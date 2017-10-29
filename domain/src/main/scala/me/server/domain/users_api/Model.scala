package me.server.domain.users_api

case class UserId(id: Long) {
  def asLong: Long = id
}

case class User(loginEmail: String, password: String, personInfo: PersonInfo, active: Boolean)

object User {
  val empty = User("","",PersonInfo.empty,true)
}

case class PersonInfo(firstName:String, lastName: String, email: String, phone: String, personalData: Option[PersonalData])

object PersonInfo {
  val empty = PersonInfo("", "", "", "", None)
}

case class PersonalData(adress: String)