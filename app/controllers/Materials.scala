package controllers

import play.api._
import play.api.mvc._
import play.api.data.Forms._
import play.api.data._
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
import jp.t2v.lab.play2.auth._
import models._
import views._ 


object Materials extends Controller with AuthElement with AuthConfigImpl {

  val materialForm = Form (
  	mapping (
  		"id" -> optional(longNumber),
  		"name" -> nonEmptyText,
  		"createDate" -> optional(date("yyyy-MM-dd")),
  		"tAmount" -> optional(of[Float]),
      "compID" -> longNumber
  		)((id, name, createDate, tAmount, compID) =>
  		Material(id, name, DbApi.date2sql(createDate), tAmount, compID))
  		((m: Material) => Some((m.id, m.name, m.createDate.asInstanceOf[Option[java.util.Date]], m.tAmount, m.compID)))
  	)

  
  def create(compId: Long) = StackAction(AuthorityKey -> LocalAdministrator){ 
    implicit rs => 
     Ok(html.material.create(compId: Long, materialForm))
  }


  def save(compId: Long) = StackAction(AuthorityKey -> LocalAdministrator){ 
    implicit rs => 
    println(compId + " save material ")
    materialForm.bindFromRequest.fold(
    formWithErrors => BadRequest(html.material.create(compId, formWithErrors)),
      mat => { 

        println(mat + " save material ")
        DbApi.insertMat(compId, mat)
      Redirect(routes.ResolveUser.index).flashing("success" -> s" ${mat.name} został dodany")
        }
      )
  }


}