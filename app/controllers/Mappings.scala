package controllers

import models.{Address, SignedUserData, UserData}
import play.api.data.Form
import play.api.data.Forms._

object Mappings {

  val userForm: Form[UserData] = Form(
    mapping(
      "firstName" -> nonEmptyText,
      "middleName" -> optional(text),
      "lastName" -> nonEmptyText,
      "email" -> email,
      "contact" -> nonEmptyText(maxLength = 10), //.verifying("Contact should contain Digits only",contact => contact.equals("""[0-9.+]+""".r.toString())), //text.verifying("Contact Number Should contain digits",contact.equals("""[0-9.+]+""".r)),
      "age" -> number(min = 18, max = 75),
      "address" -> mapping(
        "city" -> nonEmptyText,
        "state" -> nonEmptyText
      )(Address.apply)(Address.unapply),
      "gender" -> text,
      "hobbies" -> list(text),
      "userName" -> nonEmptyText,
      "password" -> nonEmptyText(maxLength = 8),
      "confirmPassword" -> nonEmptyText(maxLength = 8),
      "isAdmin" -> boolean,
      "status" -> boolean
    )(UserData.apply)(UserData.unapply) verifying("Passwords don't match", data => data.password.equals(data.confirmPassword))
  )

  val signInForm: Form[SignedUserData] = Form(
    mapping(
      "userName" -> nonEmptyText,
      "password" -> nonEmptyText(maxLength = 8)
    )(SignedUserData.apply)(SignedUserData.unapply))

  /*def validateContact(contact: String) = {
    if(contact.length == 10) {
      if(contact.equals([0-9.+]+""".r.toString()))
    }
  }*/

}
