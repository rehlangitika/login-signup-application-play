package controllers

import javax.inject.Inject

import models.{Address, SignedUserData, UserData}
import play.api.data.Forms._
import play.api.data._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller}

class LoginController @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport {

  /*Action when user clicks on the submit button for SignUp*/
  def createUser() = Action { implicit request =>
    val registerForm = LoginController.userForm.bindFromRequest()
    registerForm.fold(
      hasErrors => {
        println(hasErrors)
        Redirect(routes.LoginController.register()).flashing("error" -> "Fill Details")
      },
      success = {
        implicit registeredUser => Ok(views.html.profile(registeredUser)).withSession("registeredUserSession" -> registeredUser.firstName)
      }
    )
  }

  /*Action for uploading the SignUp form */
  def register() = Action { implicit request =>
    Ok(views.html.user(LoginController.userForm))
  }

  /*Action for providing access to the user for suucessful login
  * Used dummy (hardcode) data for checking and redirecting to profile page */
  def getAccess() = Action {
    implicit request =>
      val newSignInForm = LoginController.signInForm.bindFromRequest()
      newSignInForm.fold(
        hasErrors => {
          Redirect(routes.LoginController.signIn()).flashing("error" -> "Fill Email Id and Password")
        },
        success = { implicit newUser =>
          if (newUser.email.equals("gitika.rehlan@knoldus.in") && newUser.password.equals("gitika31")) {
            Ok(views.html.profile(UserData("Gitika", None, "Rehlan", "gitika.rehlan@knoldus.in",
              "9999747608", 23, Address("New Delhi", "Delhi"), "Female", List("Singing"), "gitika31", "gitika31"))).withSession("memberSession" -> "gitika.rehlan@knoldus.in")
          }
          else {
            Redirect(routes.LoginController.signIn()).flashing("error" -> "Invalid User!")
          }
        }
      )
  }

  /*Action for uploading the SignIn form */
  def signIn() = Action {
    implicit request => Ok(views.html.signin(LoginController.signInForm))
  }

  /*Action for uploading an image file */
  def upload() = Action(parse.multipartFormData) { implicit request =>
    request.body.file("picture").map { picture =>
      import java.io.File
      val filename = picture.filename
      println(filename)
      val contentType = picture.contentType
      picture.ref.moveTo(new File(s"/tmp/picture/$filename"))
      Redirect(routes.LoginController.register()).flashing("success" -> "File Uploaded Successfully")
    }.getOrElse {
      Redirect(routes.LoginController.signIn()).flashing("error" -> "File not uploaded")
    }
  }


  def fileUpload() = Action {
   implicit request => Ok(views.html.uploadfile())
  }

}

object LoginController {

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
      "password" -> nonEmptyText(maxLength = 8),
      "confirmPassword" -> nonEmptyText(maxLength = 8)
    )(UserData.apply)(UserData.unapply) verifying("Passwords don't match", data => data.password.equals(data.confirmPassword))
  )

  val signInForm: Form[SignedUserData] = Form(
    mapping(
      "email" -> email,
      "password" -> nonEmptyText(minLength = 8)
    )(SignedUserData.apply)(SignedUserData.unapply))

  /*def validateContact(contact: String) = {
    if(contact.length == 10) {
      if(contact.equals([0-9.+]+""".r.toString()))
    }
  }*/
}
