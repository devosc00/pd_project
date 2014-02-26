package models

import play.api.db.slick.Config.driver.simple._
import scala.reflect.runtime.universe._
import play.api.db.DB
import play.api.Play.current
import java.util.Date
import java.sql.{ Date => SqlDate }
import scala.slick.lifted.Tag
import java.sql.Timestamp


case class Project (
	id: Option[Long],
	name: String,
	startDate: Date,
	endDate: Date,
	ordered: Int,
	matAmount: Float,
	doneParts: Int,
	accID: Long,
	matID: Long )

class Projects (tag: Tag) extends Table[Project] (tag, "PROJECT") {
	
	implicit val dateColumnType = MappedColumnType.base[Date, Long](d => d.getTime, d => new Date(d))

	def id = column[Long] ("ID", O.PrimaryKey, O.AutoInc)
	def name = column[String] ("NAME")
	def startDate = column [Date] ("START_DATE")
	def endDate = column [Date]("END_DATE")
	def ordered = column [Int] ("ORDERED")
	def matAmount = column [Float] ("MAT_AMOUNT")
	def doneParts = column [Int] ("DONE_PARTS")
	def accID = column [Long] ("ACC_ID")
	def matID = column [Long] ("MAT_ID")

	def * = (id.?, name, startDate, endDate, ordered, matAmount, doneParts, 
		accID, matID) <> (Project.tupled, Project.unapply)

	def accountFK = foreignKey("ACC_FK", accID, DbApi.accounts)(_.id)
	def materialFK = foreignKey("MAT_FK", matID, DbApi.materials)(_.id)
}