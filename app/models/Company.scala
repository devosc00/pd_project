package models

import play.api.db.slick.Config.driver.simple._
import scala.reflect.runtime.universe._
import play.api.db.DB
import play.api.Play.current
//import java.util.Date
import java.sql.Date
import scala.slick.lifted.Tag
import java.sql.Timestamp


case class Company(
  id: Option[Long],
  name: String,
  city: String,
  street: String,
  phone: String,
  createDate: Option[Date],
  orders: Option[Int])


class Companies(tag: Tag) extends Table[Company] (tag, "COMPANY") {
  
  //implicit val dateColumnType = MappedColumnType.base[Date, Long](d => d.getTime, d => new Date(d))

  def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)
  def name = column[String] ("NAME", O.NotNull)
  def city = column[String] ("CITY")
  def street = column [String] ("STREET")
  def phone = column[String] ("PHONE")
  def createDate = column[Date] ("CREATE_DATE")
  def orders = column[Int] ("ORDERS")

  def * = (id.?, name, city, street, phone, createDate.?, orders.?) <> (Company.tupled, Company.unapply)
  }

  
