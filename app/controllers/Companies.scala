package controllers

import play.api._
import play.api.mvc._
import play.api.data.Forms._
import play.api.data._
import models.Company

import java.util.{ Date }
/** Uncomment the following lines as needed **/
/**
import play.api.Play.current
import play.api.libs._
import play.api.libs.iteratee._
import play.api.libs.concurrent._
import java.util.concurrent._
import scala.concurrent.stm._
import akka.util.duration._
import play.api.cache._
import play.api.libs.json._
**/

object Companies extends Controller {

  val companyForm = Form (
  	mapping(
  		"id" -> optional(longNumber),
  		"name" -> nonEmptyText,
  		"city" -> nonEmptyText,
  		"street" -> nonEmptyText,
  		"phone" -> nonEmptyText,
  		"createDate" -> optional(date("yyyy-MM-dd")),
  		"orders" -> number 
  		)((id, name, city, street, phone, createDate, orders) =>
      Company(id, name, city, street, phone, createDate.asInstanceOf[Option[java.sql.Date]], orders))
      ((c: Company) => Some((c.id, c.name, c.street, c.street, c.phone, c.createDate.asInstanceOf[Option[java.util.Date]], c.orders)))
  	)  

}