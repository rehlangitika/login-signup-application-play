package controllers

import javax.inject.Inject

import com.google.inject.name.Named
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller}
import services.UserService

class RegisterController @Inject()(@Named("cache")userService: UserService)(val messagesApi: MessagesApi) extends Controller with I18nSupport {

  /*Action when user clicks on the submit button for SignUp and then redirecting to Profile Page*/
  def createUser() = Action { implicit request =>
    val registerForm = Mappings.userForm.bindFromRequest()
    registerForm.fold(
      hasErrors => {
        //println(hasErrors)
        Redirect(routes.RegisterController.register()).flashing("error" -> "Fill Details")
      },
      success = {
        implicit registeredUser =>
          println(checkUserType)
          val user = userService.storeUserData(registeredUser)
          //println("1." + registeredUser)
          Redirect(routes.ProfileController.profile()).withSession("registeredUsers" -> registeredUser.userName)
      }
    )
  }

  /*Action for uploading the SignUp form */
  def register() = Action { implicit request =>
    Ok(views.html.user(Mappings.userForm))
  }

  def checkUserType: String = {
    if(play.Play.application().configuration().getString("Type") == "Admin") {
      s"Admin"
    }
    else s"Normal User"
  }
}
