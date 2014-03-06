package controllers

import play.api._
import play.api.mvc._
import play.api.data.Forms._
import play.api.data._
import models._
import play.api.data.format.Formats._
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

object Materials extends Controller {

  val materialForm = Form (
  	mapping (
  		"id" -> optional(longNumber),
  		"name" -> nonEmptyText,
  		"createDate" -> optional(date("yyyy-MM-dd")),
  		"tAmount" -> of[Float]
  		)((id, name, createDate, tAmount) =>
  		Material(id, name, createDate.asInstanceOf[Option[java.sql.Date]], tAmount))
  		((m: Material) => Some((m.id, m.name, m.createDate.asInstanceOf[Option[java.util.Date]], m.tAmount)))
  	)

  

}