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


object Projects extends Controller with AuthElement with AuthConfigImpl {



  val projForm = Form(
  	mapping(
  		"id" -> optional(longNumber),
  		"name" -> optional(text),
  		"startDate" -> optional(date("yyyy-MM-dd")),
  		"endDate" -> optional(date("yyyy-MM-dd")),
  		"ordered" -> default(number,0),
  		"matAmount" -> optional(of[Float]),
  		"doneParts" -> default(number,0),
  		"accID" -> optional(longNumber),
  		"matID" -> optional(longNumber)
  		)((id, name, startDate, endDate, ordered, matAmount, doneParts, accID, matID) =>
  		Project(id, name, DbApi.date2sql(startDate), DbApi.date2sql(endDate),
  			Option(ordered), matAmount, Option(doneParts), accID, matID))
  		((p: Project) => Some((p.id, p.name, p.startDate.asInstanceOf[Option[java.util.Date]], 
  			p.endDate.asInstanceOf[Option[java.util.Date]], p.ordered.get, p.matAmount, p.doneParts.get, p.accID, p.matID)))
  		)


  def projList(id: Long, page: Int, orderBy: Int, filterr: String) = StackAction(AuthorityKey -> LocalAdministrator){ 
    implicit rs => 
     Ok(html.project.projectsList(
      DbApi.projList(id, page = page, orderBy = orderBy, filterr = ("%"+filterr+"%")),
      orderBy, filterr, id )
    ) 
  }


  def operatorProjList(id: Long, page: Int, orderBy: Int, filterr: String) = StackAction(AuthorityKey -> Operator){ 
    implicit rs => 
     Ok(html.project.operatorProjList(
      DbApi.operatorProjList(id, page = page, orderBy = orderBy, filterr = ("%"+filterr+"%")),
      orderBy, filterr, id )
    ) 
  }


  def salesProjList(id: Long, page: Int, orderBy: Int, filterr: String) = StackAction(AuthorityKey -> SalesCraft){ 
    implicit rs => 
     Ok(html.project.salesProjList(
      DbApi.projList(id, page = page, orderBy = orderBy, filterr = ("%"+filterr+"%")),
      orderBy, filterr, id )
    ) 
  }


  def create(compId: Long) = StackAction(AuthorityKey -> LocalAdministrator){ 
    implicit rs => 
   	 Ok(html.project.create(compId: Long, projForm, DbApi.optionsAccOperator(compId), DbApi.optionsMat(compId)))
  }


  def saveNew(compId: Long) = StackAction(AuthorityKey -> LocalAdministrator){ 
    implicit rs => 
		projForm.bindFromRequest.fold(
		formWithErrors => BadRequest(html.project.create(compId, formWithErrors, DbApi.optionsAcc(compId), DbApi.optionsMat(compId))),
			proj => { DbApi.insertProj(proj)
			Redirect(routes.ResolveUser.index).flashing("success" -> s" ${proj.name} został dodany")
				}
			)
  }


  def start(id: Long) = StackAction(AuthorityKey -> LocalAdministrator){ 
      val compId = DbApi.getCompId(id)
    implicit rs => 
      DbApi.findProjById(id) match {
        case Some(e) => Ok(html.project.start(id, compId ,projForm.fill(e), DbApi.optionsAccOperator(compId), DbApi.optionsMat(compId)))
        case None => NotFound
      }
   	 
  }


  def saveStart(id: Long) = StackAction(AuthorityKey -> LocalAdministrator){ 
     	val compId = DbApi.getCompId(id) 
    implicit rs =>
		projForm.bindFromRequest.fold(
		formWithErrors => BadRequest(html.project.start(id, compId, formWithErrors, DbApi.optionsAccOperator(compId), DbApi.optionsMat(compId))),
			proj => { DbApi.newStartProject(id, proj)
				Redirect(routes.ResolveUser.index).flashing("success" -> s" ${proj.name} wystartował")
				}
			)
  		}


  def editDoneParts(id: Long) = StackAction(AuthorityKey -> Operator) { 
  	implicit rs =>
  	  	DbApi.findProjById(id) match {
  	  	case Some(e) => Ok(html.project.updateParts(id, projForm.fill(e)))
  	  	case None => NotFound
  	  }
  }


 def saveDoneParts(id: Long) = StackAction(AuthorityKey -> Operator) {
 	val longId = DbApi.findProjById(id).get.id
  //println(longId) 
  	implicit rs =>
  	projForm.bindFromRequest.fold(
  		formWithErrors => BadRequest(html.project.updateParts(id, formWithErrors)),
  		parts => { 
        println("doneParts " +  parts.doneParts)
  			DbApi.updateDoneParts(parts.id, parts.name , parts.doneParts)
  			Redirect(routes.ResolveUser.index).flashing("success" -> s" ${parts.name} uaktualniona liczba wykonanych sztuk")
  			}
  		)
  }


  def delete(id: Long) = StackAction(AuthorityKey -> LocalAdministrator) { implicit rs =>
    DbApi.deleteProj(id)
    Redirect(routes.ResolveUser.index).flashing("success" -> s"Został usunięty")
  }


}