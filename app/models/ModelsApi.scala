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

  def date2sql(d: Option[java.util.Date]): Option[java.sql.Date] = {
    d.map(d => new java.sql.Date(d.getTime()))
  } 

  def optionsMat(id: Long): Seq[(String, String)] = {
    DB.withSession { implicit session =>
    val query = (for {
      material <- materials.filter(_.compID === id)
      } yield (material.id, material.name)).sortBy(_._2)
    	query.list.map(row => (row._1.toString, row._2))
    }
  }

  def optionsComp: Seq[(String, String)] = {
    DB.withSession { implicit session =>
    val query = (for {
      company <- companies
      } yield (company.id, company.name)).sortBy(_._2)
      query.list.map(row => (row._1.toString, row._2))
    }
  }


  def optionsAcc(id: Long): Seq[(String, String)] = {
    DB.withSession { implicit session =>
    val query = (for {
      acc <- accounts.filter(_.compID === id)
      } yield (acc.id, acc.name)).sortBy(_._2)
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



 def findProjById(id: Long): Option[Project] = {
      DB.withSession { implicit session =>
      projects.where(_.id === id).firstOption
    }
  }

//in project id, out company id 
  def getCompId(id: Long): Long = {
    DB.withSession { implicit session =>
   val aID = projects.filter(_.id === id)first
   val a = aID.accID
   val compId = (for { acc <- accounts if acc.id === a } yield acc.compID )
    compId.first
     }
  }


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


  def projList(id: Long, page: Int = 0, pageSize: Int = 10, orderBy: Int = 1, filterr: String = "%"): Page[(Project, Account)] = {
    DB.withSession { implicit session =>
    val offset = pageSize * page
    val query =
      (for {
        (project, account) <- projects leftJoin accounts.filter(_.compID === id) on (_.accID === _.id)
        if project.name.toLowerCase like filterr.toLowerCase()
      } yield (project, account))
        .drop(offset)
        .take(pageSize)

    val totalRows = count(filterr)
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


  def newStartProject(id: Long, proj: Project) {
    DB.withSession { implicit session =>
      val x = findProjById(id)
      val projToInsert = Project(x.get.id, x.get.name, proj.startDate, proj.endDate, proj.ordered, proj.matAmount,
       proj.doneParts, proj.accID, proj.matID)
      println ("new proj " + projToInsert)
      projects.where(_.id === id).update(projToInsert)
    }
  } 


  def insertProj(proj: Project) {
    DB.withSession { implicit session =>
      println(proj)
      projects.insert(proj)
    }
  }

  var newCompID: Long = 0

  def insertComp(comp: Company) {
    DB.withSession { implicit session =>
      println(comp)
      newCompID = (companies returning companies.map(_.id)) insert(comp)
      println(newCompID)
      
    }
  }

/*  def findEmptyComp: Long = {
    DB.withSession { implicit session =>
      val query = (for { c <- companies
                         a <- accounts if !(c.id === a.compID) }
                         yield c) 
      println(query.selectStatement + " data " + query.first + " list " + query.list)
      val id = (for  { q <- query
                       c <- companies if q.id != c.id }
                       yield c.id) 
      println(id.selectStatement + " data " + id.first + " list " + id.list)
      id.first
    }
  }*/

  def delete(id: Long) = {
    DB.withSession { implicit session =>
    accounts.where(_.id === id).delete
    }
  }
  

}