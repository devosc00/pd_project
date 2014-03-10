package models

import play.api.db.slick.Config.driver.simple._
import scala.reflect.runtime.universe._
import play.api.db.DB
import play.api.Play.current
//import java.util.Date
import java.sql.Date
import scala.slick.lifted.Tag
import java.sql.Timestamp

case class Material (
	id: Option[Long],
	name: String,
	createDate: Option[Date],
	tAmount: Option[Float],
	compID: Long )

class Materials (tag: Tag) extends Table [Material] (tag, "MATERIAL") {

	//implicit val dateColumnType = MappedColumnType.base[Date, Long](d => d.getTime, d => new Date(d))

	def id = column [Long] ("ID", O.PrimaryKey, O.AutoInc)
	def name = column [String] ("NAME")
	def createDate = column [Date] ("DATE")
	def tAmount = column [Float] ("TOTAL_AMOUNT")
	def compID = column [Long] ("COMP_ID")

	def * = (id.?, name, createDate.?, tAmount.?, compID) <> (Material.tupled, Material.unapply)

	def companyFK = foreignKey("COMP_FK", compID, DbApi.companies)(_.id)
}