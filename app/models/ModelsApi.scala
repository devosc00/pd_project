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

  def insert (acc: Account) = {
    DB.withSession { implicit session =>
  	accounts.insert(acc)
    }
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

  def optionsMat(implicit s: Session): Seq[(String, String)] = {
    val query = (for {
      material <- materials
      } yield (material.id, material.name)).sortBy(_._2)

    	query.list.map(row => (row._1.toString, row._2))
  }

  def optionsComp: Seq[(String, String)] = {
    DB.withSession { implicit session =>
    val query = (for {
      company <- companies
      } yield (company.id, company.name)).sortBy(_._2)
      query.list.map(row => (row._1.toString, row._2))
    }
  }


  def authenticate(email: String, password: Option[String]): Option[Account] = { 
    DB.withSession { implicit session =>
    findByEmail(email).filter { account => password.equals(account.password) }
    }
  }

/*  def getCompId (a: Account)(implicit s: Session): Long = {
     user => user.get.compID
  }*/


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


/*  def findByPk(id: Long) = {
    DB.withSession { implicit session =>
    for (n <- accounts if n.id === id) yield n
    }
  }*/


  def count(implicit s: Session): Int =
    Query(accounts.length).first

 
  def count(filter: String)(implicit s: Session): Int =
    Query(accounts.where(_.name.toLowerCase like filter.toLowerCase).length).first


  def usersList(page: Int = 0, pageSize: Int = 10, orderBy: Int = 1, filter: String = "%"): Page[(Account, Company)] = {
    DB.withSession { implicit session =>
    val offset = pageSize * page
    val query =
      (for {
        (account, company) <- accounts leftJoin companies on (_.compID === _.id)
        if account.name.toLowerCase like filter.toLowerCase()
      } yield (account, company))
        .drop(offset)
        .take(pageSize)

    val totalRows = count(filter)
    val result = query.list.map(row => (row._1, row._2))

    Page(result, page, offset, totalRows)
    }
  }


  def projList(id: Long, page: Int = 0, pageSize: Int = 10, orderBy: Int = 1, filter: String = "%"): Page[(Project, Account)] = {
    DB.withSession { implicit session =>
    val offset = pageSize * page
    val query =
      (for {
        (project, account) <- projects leftJoin accounts.filter(_.compID === id) on (_.accID === _.id)
        if project.name.toLowerCase like filter.toLowerCase()
      } yield (project, account))
        .drop(offset)
        .take(pageSize)

    val totalRows = count(filter)
    val result = query.list.map(row => (row._1, row._2))

    Page(result, page, offset, totalRows)
    }
  }


 def update(id: Long, acc: Account) {
    DB.withSession { implicit session =>
    val x = findById(id)
    val accToUpdate: Account = new Account(x.get.id, acc.email, x.get.password, acc.name, 
      acc.position, acc.permission, x.get.compID)
    println("copy " + accToUpdate)
    accounts.where(_.id === id).update(accToUpdate)
    }
  }


def delete(id: Long) = {
    DB.withSession { implicit session =>
    accounts.where(_.id === id).delete
    }
  }
  

}