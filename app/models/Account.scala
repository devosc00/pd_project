package models

import play.api.db.slick.Config.driver.simple._
import play.api.db.DB
import play.api.Play.current
import scala.reflect.runtime.universe._
import scala.slick.lifted.Tag


case class Account (
  id: Option[Long], 
  email: String, 
  password: Option[String], 
  name: String,
  position: String, 
  permission: Permission, 
  compID:Option[Long] )


class Accounts(tag: Tag) extends Table[Account](tag, "ACCOUNT") {
  def id = column[Long] ("ID", O.PrimaryKey, O.AutoInc)
  def email = column[String] ("EMAIL", O.NotNull)
  def password = column[String] ("PASS", O.NotNull)
  def name = column[String] ("NAME", O.NotNull)
  def position = column[String] ("POSITION")
  def permission = column[Permission] ("PERMISSION", O.NotNull)
  def compID = column[Long] ("COMP_ID")

  def * = (id.?, email, password.?, name, position, permission, compID.?) <> (Account.tupled, Account.unapply)

  def companyFK = foreignKey("COMP_FK", compID, DbApi.companies)(_.id)
  }


  