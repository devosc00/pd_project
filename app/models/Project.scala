package models

import play.api.db.slick.Config.driver.simple._
import scala.reflect.runtime.universe._
import play.api.db.DB
import play.api.Play.current
//import java.util.dateColumnTypee
import java.sql.Date
import scala.slick.lifted.Tag
import java.sql.Timestamp


case class Project (
	id: Option[Long],
	name: Option[String],
	startDate: Option[Date],
	endDate: Option[Date],
	ordered: Option[Int],
	matAmount: Option[Float],
	doneParts: Option[Int],
	accID: Option[Long],
	matID: Option[Long] )

class Projects (tag: Tag) extends Table[Project] (tag, "PROJECT") {
	
	//implicit val dateColumnType = MappedColumnType.base[Date, Long](d => d.getTime, d => new Date(d))

	def id = column[Long] ("ID", O.PrimaryKey, O.AutoInc)
	def name = column[String] ("NAME", O.Nullable)
	def startDate = column [Date] ("START_DATE", O.Nullable)
	def endDate = column [Date]("END_DATE", O.Nullable)
	def ordered = column [Int] ("ORDERED", O.Nullable)
	def matAmount = column [Float] ("MAT_AMOUNT", O.Nullable)
	def doneParts = column [Int] ("DONE_PARTS", O.Nullable)
	def accID = column [Long] ("ACC_ID")
	def matID = column [Long] ("MAT_ID")

	def * = (id.?, name.?, startDate.?, endDate.?, ordered.?, matAmount.?, doneParts.?, 
		accID.?, matID.?) <> (Project.tupled, Project.unapply)

	def accountFK = foreignKey("ACC_FK", accID, DbApi.accounts)(_.id)
	def materialFK = foreignKey("MAT_FK", matID, DbApi.materials)(_.id)
}