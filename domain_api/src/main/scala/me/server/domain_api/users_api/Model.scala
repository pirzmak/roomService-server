package me.server.domain_api.users_api

case class UserId(id: Long) {
  def asLong: Long = id
}

sealed trait UserType{def name: String}

case object Owner extends UserType{def name = "Owner"}
case object Receptionist extends UserType{def name = "Receptionist"}

case class User(loginEmail: String, password: String, personInfo: PersonInfo, userType: UserType, emailConfirmed: Boolean, active: Boolean)

object User {
  val empty = User("","",PersonInfo.empty, Receptionist, true, true)
}

case class PersonInfo(firstName:String, lastName: String, email: String, phone: String, personalData: Option[PersonalData])

object PersonInfo {
  val empty = PersonInfo("", "", "", "", None)
}

case class PersonalData(adress: String, sex: String, age: Int, Id: String)

object PersonalData {
  val empty = PersonalData("", "", 0, "")
}