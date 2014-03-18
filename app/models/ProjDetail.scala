package models

import play.api.db.slick.Config.driver.simple._
import java.sql.Date
import play.api.db.DB
import play.api.Play.current
//import java.util.Date
import scala.slick.lifted.Tag
import java.sql.Timestamp
//import org.joda.time.DateTime
 


case class ProjDetail (
	id: Option [Long],
	name: String,
	totalStartDate: Option[Date],
	oneItemTime: Float,
	totalItems: Int,
	saldo: Int,
	orderCouter: Int,
	totalMat: Float,
	projID: Long )


class ProjDetails (tag: Tag) extends Table [ProjDetail] (tag, "PROJ_DETAIL") {

	//implicit val dateColumnType = MappedColumnType.base[Date, Long](d => d.getTime, d => new Date(d))

	def id = column [Long] ("ID", O.PrimaryKey, O.AutoInc)
	def name = column [String] ("NAME")
	def totalStartDate = column [Date] ("TOTAL_START")
	def oneItemTime = column [Float] ("ITEM_TIME")
	def totalItems = column [Int] ("TOTAL_ITEMS")
	def saldo = column [Int] ("SALDO")
	def orderCouter = column [Int] ("ORDER_COUTER")
	def totalMat = column [Float] ("TOTAL_MAT")
	def projID = column [Long] ("PROJ_ID")

	def * = (id.?, name, totalStartDate.?, oneItemTime, totalItems, saldo, orderCouter, 
		totalMat, projID) <> (ProjDetail.tupled, ProjDetail.unapply)

	def projectFK = foreignKey ("PROJ_FK", projID, DbApi.projects)(_.id)	
}