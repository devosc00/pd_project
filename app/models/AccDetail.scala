package models

import play.api.db.slick.Config.driver.simple._
import scala.reflect.runtime.universe._
import play.api.db.DB
import play.api.Play.current
import java.util.Date
import java.sql.{ Date => SqlDate }
import scala.slick.lifted.Tag
import java.sql.Timestamp

case class AccDetail (
	id: Option[Long],
	startDate: Date,
	projectCouter: Int,
	accID: Long,
	projDetailID: Long )


class AccDetails (tag: Tag) extends Table [AccDetail] (tag, "ACC_DETAIL") {

	implicit val dateColumnType = MappedColumnType.base[Date, Long](d => d.getTime, d => new Date(d))

	def id = column [Long] ("ID", O.PrimaryKey, O.AutoInc)
	def startDate = column [Date] ("START_DATE")
	def projectCouter = column [Int] ("PROJECT_COUTER")
	def accID = column [Long] ("ACC_ID")
	def projDetailID = column [Long] ("PROJ_DETAIL_ID")

	def * = (id.?, startDate, projectCouter, accID, projDetailID) <> (AccDetail.tupled, AccDetail.unapply)

	def accountFK = foreignKey ("ACCOUNT_FK", accID, DbApi.accounts)(_.id)
	def projDetailFK = foreignKey ("PROJ_DETAIL_FK", projDetailID, DbApi.projDetails)(_.id)

}