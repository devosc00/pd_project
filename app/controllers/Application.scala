package controllers

import play.api._
import play.api.mvc._
import play.api.data.Forms._
import play.api.data._
import play.api.mvc.Results._
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

import jp.t2v.lab.play2.stackc.{ RequestWithAttributes, RequestAttributeKey, StackableController }
import jp.t2v.lab.play2.auth._
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import reflect.{ClassTag, classTag}

import models._
import views._


object Application extends Controller with LoginLogout with AuthConfigImpl {




  val loginForm = Form {
    mapping("email" -> email, "password" -> text)(DbApi.authenticate)(_.map(u => (u.email, "")))
      .verifying("Błędny adres email lub hasło", result => result.isDefined)
  }


/*  def index = Action { implicit request => 
    val user = loggedIn
    println(user)
    Ok ("hello index")
  }*/

  def login = Action { implicit request =>
    println("start login")
    Ok(html.login(loginForm))
  }

  def logout = Action.async { implicit request =>
    gotoLogoutSucceeded.map(_.flashing( "success" -> "Zostałeś wylogowany"))
  }

  def authenticate = Action.async { implicit request =>
    println("start act")
    loginForm.bindFromRequest.fold(
      formWithErrors => { println("form eror") 
        Future.successful(BadRequest(html.login(formWithErrors))) },
      user           => gotoLoginSucceeded(user.get.id.getOrElse(0.asInstanceOf[Long])))
  }
}

trait AuthConfigImpl extends AuthConfig {

  type Id = Long

  type User = Account

  type Authority = Permission

  val idTag = classTag[Id]

  val sessionTimeoutInSeconds = 3600

  def resolveUser(id: Id)(implicit ctx: ExecutionContext) = Future.successful(DbApi.findById(id))

  def loginSucceeded(request: RequestHeader)(implicit ctx: ExecutionContext) = 
    Future.successful(Redirect(routes.ResolveUser.index))

  def logoutSucceeded(request: RequestHeader)(implicit ctx: ExecutionContext) = 
    Future.successful(Redirect(routes.Application.login))

  def authenticationFailed(request: RequestHeader)(implicit ctx: ExecutionContext) = 
    Future.successful(Redirect(routes.Application.login))

  def authorizationFailed(request: RequestHeader)(implicit ctx: ExecutionContext) = 
    Future.successful(Forbidden("brak dostępu"))

  def authorize(user: User, authority: Authority)(implicit ctx: ExecutionContext) = 
    Future.successful ((user.permission, authority) match {
    case (Administrator, _) => true
    case (LocalAdministrator, LocalAdministrator) => true
    case (Operator, Operator) => true
    case (SalesCraft, SalesCraft) => true
    case _ => false
  })

  /**
   * Whether use the secure option or not use it in the cookie.
   * However default is false, I strongly recommend using true in a production.
   */
  override lazy val cookieSecureOption: Boolean = play.api.Play.isProd(play.api.Play.current)

}
