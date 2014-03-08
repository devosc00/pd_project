package controllers

import play.api._ 
import play.api.mvc._
import play.api.data.Forms._
import play.api.data._
import play.api.Play.current
import play.api.db.slick.Config.driver.simple._


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
import play.api.db.slick._
import jp.t2v.lab.play2.auth._
import models._
import views._  

object ResolveUser extends Controller with AuthenticationElement with AuthConfigImpl {


//widoki po zalogowaniu w zależności od użytkownika
  def index = StackAction { implicit request =>
    val user: User = loggedIn
    println(user.compID.get)
    val perm: SimpleResult = user.permission match {
    	case Administrator => Redirect(routes.Users.list(0, 2, ""))
    	case LocalAdministrator => Redirect(routes.Projects.projList(user.compID.get, 0, 2, ""))
    	case Operator => Redirect(routes.Projects.projList(user.compID.get, 0, 2, "")) 
    	case SalesCraft => Redirect(routes.Projects.projList(user.compID.get, 0, 2, ""))
    	case _ => Redirect(routes.Application.login)
    }
    perm
  }

}



object Users extends Controller with AuthElement with AuthConfigImpl { 


 val accForm = Form[Account](
    mapping(
      "id" -> optional(longNumber),
      "email" -> nonEmptyText,
      "password" -> tuple(
          "main" -> optional(text(minLength = 6)),
          "confirm" -> optional(text)
      ).verifying(
        "Hasła są różne", passwords => passwords._1 == passwords._2 ),
      "name" -> nonEmptyText,
      "position" -> nonEmptyText,
      "permission" -> text,
      "compID" -> optional(longNumber)
      )((id, email, password, name, position, permission, compID) => 
         Account(id, email, password._1, name, position, Permission.valueOf(permission),compID ))
       ((a: Account) => Some((a.id, a.email, (a.password, Option[String]("")), a.name, a.position, a.permission.toString, a.compID)))
        )

 val upForm = Form(
    mapping(
      "id" -> optional(longNumber),
      "email" -> text,
      "password" -> optional(text),
      "name" -> text,
      "position" -> text,
      "permission" -> text,
      "compID" -> optional(longNumber)
      )((id, email, password, name, position, permission, compID) => 
         Account(id, email, password, name, position, Permission.valueOf(permission),compID ))
       ((a: Account) => Some((a.id, a.email, a.password, a.name, a.position, a.permission.toString, a.compID)))
        )


  def list(page: Int, orderBy: Int, filter: String) = StackAction(AuthorityKey -> Administrator){ 
 	 implicit rs => 
    Ok(html.account.list(
      DbApi.usersList(page = page, orderBy = orderBy, filter = ("%"+filter+"%")),
      orderBy, filter )
    )
	
  }


  def create = StackAction(AuthorityKey -> Administrator) { implicit rs =>
        { 
          Ok(html.account.createForm(accForm, DbApi.optionsComp))
        }
  }


  def save = StackAction(AuthorityKey -> Administrator) { implicit rs =>
      accForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.account.createForm(formWithErrors, DbApi.optionsComp)),
      entity => { 
        
          DbApi.insert(entity)
          Redirect(routes.Users.list(0, 2, "")).flashing("success" -> s" ${entity.name} został dodany")
        
      })
  }


  def createLocalAdmin = StackAction(AuthorityKey -> Administrator) { implicit rs =>
        { 
          Ok(html.account.createLocalAdmin(accForm, DbApi.newCompID))
        }
  }


  def saveLocalAdmin(id: Long) = StackAction(AuthorityKey -> Administrator) { implicit rs =>
      accForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.account.createLocalAdmin(formWithErrors, id)),
      entity => { 
          DbApi.insert(entity)
          Redirect(routes.Users.list(0, 2, "")).flashing("success" -> s" ${entity.name} został dodany")
        
      })
  }

 
  def edit(pk: Long) = StackAction(AuthorityKey -> Administrator) { implicit rs =>
     {
 	DbApi.findById(pk) match {
        case Some(e) => Ok(html.account.editForm(pk, upForm.fill(e)))
        case None => NotFound
      }
    }
  }


  def update(pk: Long) = StackAction(AuthorityKey -> Administrator) { implicit rs =>
    	 {
      println("update form filled")
      upForm.bindFromRequest.fold(
        formWithErrors => BadRequest(html.account.editForm(pk, formWithErrors)),
        entity => {
          println(entity)
          DbApi.update(pk, entity)
          Redirect(routes.Users.list(0, 2, "")).flashing("success" -> s" ${entity.name} został uaktualniony")
        })
    }
  }


  def delete(pk: Long) = StackAction(AuthorityKey -> Administrator) { implicit rs =>
    	 {
      Redirect(routes.Users.list(0, 2, "")).flashing(DbApi.delete(pk) match {  
        case 0 => "failure" -> "Nie został usunięty"
        case x => "success" -> s"Został usunięty (deleted $x row(s))"
      })
    }
  }

}
