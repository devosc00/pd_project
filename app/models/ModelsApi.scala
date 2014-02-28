package models

import play.api.db.slick.Config.driver.simple._
import play.api.db.slick._
import scala.slick.lifted.Tag
import play.api.Play.current


case class Page[A](items: Seq[A], page: Int, offset: Long, total: Long) {
  lazy val prev = Option(page - 1).filter(_ >= 0)
  lazy val next = Option(page + 1).filter(_ => (offset + items.size) < total)
}


private[models] trait DAO {

	val companies = TableQuery[Companies]
	val accounts = TableQuery[Accounts]
	val accDetails = TableQuery[AccDetails]
	val materials = TableQuery[Materials]
	val projects = TableQuery[Projects]
	val projDetails = TableQuery[ProjDetails]
}



object DbApi extends DAO {

  def insert (acc: Account)(implicit s: Session){
  	accounts.insert(acc)
  }

  def insert (mat: Material)(implicit s: Session) {
  	materials.insert(mat)
  }

  def insert (com: Company)(implicit s: Session) {
  	companies.insert(com)
  }

  def insert (proj: Project)(implicit s: Session) {
  	projects.insert(proj)
  }

  def options(implicit s: Session): Seq[(String, String)] = {
    val query = (for {
      material <- materials
      } yield (material.id, material.name)).sortBy(_._2)

    	query.list.map(row => (row._1.toString, row._2))
  }


  def authenticate(email: String, password: String): Option[Account] = { 
    DB.withSession { implicit session =>
    findByEmail(email).filter { account => password.equals(account.password) }
    }
  }


  def findByEmail(email: String): Option[Account] = {
      DB.withSession { implicit session =>
      accounts.where(_.email === email).firstOption
     }
  }

  def findById(id: Long): Option[Account] = {
      DB.withSession { implicit session =>
      accounts.where(_.id === id).firstOption
    }
  }
  
}