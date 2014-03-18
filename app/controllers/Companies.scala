package controllers

import play.api._
import play.api.mvc._
import play.api.data.Forms._
import play.api.data._
import models._

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

import jp.t2v.lab.play2.auth._
import models._
import views._ 

object Companies extends Controller with AuthElement with AuthConfigImpl{

  val companyForm = Form (
  	mapping(
  		"id" -> optional(longNumber),
  		"name" -> nonEmptyText,
  		"city" -> nonEmptyText,
  		"street" -> nonEmptyText,
  		"phone" -> nonEmptyText,
  		"createDate" -> optional(date("yyyy-MM-dd")),
  		"orders" -> optional(number) 
  		)((id, name, city, street, phone, createDate, orders) =>
      Company(id, name, city, street, phone, DbApi.date2sql(createDate), orders))
      ((c: Company) => Some((c.id, c.name, c.street, c.street, c.phone, c.createDate.asInstanceOf[Option[java.util.Date]], c.orders)))
  	)  

  def create = StackAction(AuthorityKey -> Administrator) { implicit rs =>
        { 
          Ok(html.company.createComp(companyForm))
        }
  }


  def save = StackAction(AuthorityKey -> Administrator) { implicit rs =>
      companyForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.company.createComp(formWithErrors)),
      entity => { 
          DbApi.insertComp(entity)
          Redirect(routes.Users.createLocalAdmin)
        
      })
  }


 
  def edit(pk: Long) = StackAction(AuthorityKey -> Administrator) { implicit rs =>
     {
      DbApi.findCompById(pk) match {
        case Some(e) => Ok(html.company.editCompany(pk, companyForm.fill(e)))
        case None => NotFound
      }
    }
  }


  def update(pk: Long) = StackAction(AuthorityKey -> Administrator) { implicit rs =>
       {
      println("update form filled")
      companyForm.bindFromRequest.fold(
        formWithErrors => BadRequest(html.company.editCompany(pk, formWithErrors)),
        entity => {
          println(entity)
          DbApi.updateComp(pk, entity)
          Redirect(routes.ResolveUser.index).flashing("success" -> s" ${entity.name} został uaktualniony")
        })
    }
  }


  def delete(id: Long) = StackAction(AuthorityKey -> LocalAdministrator) { implicit rs =>
    DbApi.deleteCompany(id)
    Redirect(routes.ResolveUser.index).flashing("success" -> s"Został usunięty")
  }



}