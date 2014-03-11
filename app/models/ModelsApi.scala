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


  def optionsAccOperator(id: Long): Seq[(String, String)] = {
    DB.withSession { implicit session =>
    val query = (for {
      acc <- accounts.filter(_.compID === id) where (_.permission === Permission.valueOf("Operator"))
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


  def findCompById(id: Long): Option[Company] = {
      DB.withSession { implicit session =>
      companies.where(_.id === id).firstOption
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


/*  def countProj(compID: Long)(implicit s: Session): Int =
    Query(accounts.where(_.compID === compID).length).first*/

 
  def countProj(compID: Long, filter: String): Int =
    DB.withSession { implicit session => 
      val query =
      (for {
        a <- accounts if a.compID === compID
        p <- projects if p.accID === a.id
        if p.name.toLowerCase like filter.toLowerCase()
      } yield (p))
      query.list.length
    }


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
        a <- accounts if a.compID === id
        p <- projects if p.accID === a.id
        if p.name.toLowerCase like filterr.toLowerCase()
      } yield (p, a))
        .drop(offset)
        .take(pageSize)

    val totalRows = countProj(id,filterr)
      try {
    val result = query.list.map(row  => (row._1, row._2))
     Page(result, page, offset, totalRows)
      } catch {   
          case se: SlickException => println(se)
          Page(Seq.empty[(Project, Account)], page, offset, totalRows) 
      } 
    }
  }


  def operatorProjList(id: Long, page: Int = 0, pageSize: Int = 10, orderBy: Int = 1, filterr: String = "%"): Page[(Project, Account)] = {
    DB.withSession { implicit session =>
    val offset = pageSize * page
    val query =
      (for {
        a <- accounts if a.compID === id
        p <- projects if p.accID === a.id
        if p.name.toLowerCase like filterr.toLowerCase()
      } yield (p, a))
        .drop(offset)
        .take(pageSize)

    val totalRows = countProj(id,filterr)
      try {
    val result = query.list.map(row  => (row._1, row._2))
      Page(result, page, offset, totalRows)
      } catch {   
          case se: SlickException => println(se)
          Page(Seq.empty[(Project, Account)], page, offset, totalRows) 
      }
    }
  }


  def salesProjList(id: Long, page: Int = 0, pageSize: Int = 10, orderBy: Int = 1, filterr: String = "%"): Page[(Project, Account)] = {
    DB.withSession { implicit session =>
    val offset = pageSize * page
    val query =
      (for {
        a <- accounts if a.compID === id
        p <- projects if p.accID === a.id
        if p.ordered > p.doneParts
        if p.name.toLowerCase like filterr.toLowerCase()
      } yield (p, a))
        .drop(offset)
        .take(pageSize)

    val totalRows = countProj(id,filterr)
      try {
    val result = query.list.map(row  => (row._1, row._2))
      Page(result, page, offset, totalRows)
      } catch {   
          case se: SlickException => println(se)
          Page(Seq.empty[(Project, Account)], page, offset, totalRows) 
      }
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


  def insertMat(compID: Long, mat: Material) {
    println(mat + " insert material ")
    DB.withSession { implicit session =>
      println(mat)
      val matToinsert = Material(mat.id, mat.name, mat.createDate, mat.tAmount, Option(compID))
      materials.insert(matToinsert)
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


  def updateComp(compID: Long, comp: Company) {
    println(comp + " insert Company ")
    DB.withSession { implicit session =>
      println(comp)
     /* val comToUpdate = Company(comp.id, comp.name, comp.createDate, comp.tAmount, Option(compID))
      companies.insert(comToUpdate)*/
      val compToUpdate = comp.copy(Some(compID))
        companies.where(_.id === compID).update(compToUpdate)
    }
  }


  def updateDoneParts(id: Option[Long], name: Option[String], done: Option[Int]) = {
    DB.withSession { implicit session => 
      println(id.get +" " + done.get)
      val x = (for  { p <- projects if p.id === id.get } yield p.doneParts)
      val y = x.first + done.get
      println(x.first + " from db + from form " + done.get + " = update " + y )
      x.update(y) 
    }
  } 


  def updateUsedMaterial(id: Long, done: Float) {
    DB.withSession { implicit session => 
      val x = (for  { p <- projects if p.id === id } yield p.matAmount )
      val y = (x.first + done)
      x.update(y) 
    }
  }


  def delete(id: Long) = {
    DB.withSession { implicit session =>
    accounts.where(_.id === id).delete
    }
  }
  

  def deleteCompany(id: Long) = {
    DB.withSession { implicit session =>
    companies.where(_.id === id).delete
    }
  }

    def deleteProj(id: Long) = {
    DB.withSession { implicit session =>
    projects.where(_.id === id).delete
    }
  }


}