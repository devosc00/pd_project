package models

import play.api.db.slick.Config.driver.simple._
import play.api.db.DB

sealed trait Permission
case object Administrator extends Permission
case object Operator extends Permission
case object LocalAdministrator extends Permission
case object SalesCraft extends Permission

object Permission {
 implicit val PermissionTimeMapper = MappedColumnType.base[Permission, String](
    d => Permission.stringValueOf(d),
    t => Permission.valueOf(t)
    )


  def valueOf(value: String): Permission = value match {
    case "Administrator" => Administrator
    case "LocalAdministrator" => LocalAdministrator
    case "Operator" => Operator
    case "SalesCraft" => SalesCraft
    case _ => throw new IllegalArgumentException()
  }

  def stringValueOf(value: Permission): String = value match {
    case Administrator => "Administrator" 
    case LocalAdministrator => "LocalAdministrator"
    case Operator => "Operator"
    case SalesCraft => "SalesCraft" 
    case _ => throw new IllegalArgumentException()
  }

  def permOption: Seq[String] = Seq(LocalAdministrator.toString, 
    Operator.toString, SalesCraft.toString)
}