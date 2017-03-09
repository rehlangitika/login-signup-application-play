package controllers

import javax.inject.Inject

import com.google.inject.name.Named
import models.SignedUserData
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller}
import services.UserService

class LoginController @Inject()(@Named("cache") userService: UserService)(val messagesApi: MessagesApi) extends Controller with I18nSupport {

  /*Action for providing access to the user for successful login and redirecting to profile page */
  def getAccess() = Action {
    implicit request =>
      val newSignInForm = Mappings.signInForm.bindFromRequest()
      newSignInForm.fold(
        hasErrors => {
          Redirect(routes.LoginController.signIn()).flashing("error" -> "Fill Email Id and Password")
        },
        success = {
          implicit newUser: SignedUserData =>
            if (userService.checkLoggedInUser(newUser)) {
              Redirect(routes.ProfileController.profile()).withSession("registeredUsers" -> newUser.userName)
            }
            else {
              Redirect(routes.LoginController.signIn()).flashing("error" -> "Invalid User")
            }
        }

      )
  }

  /*Action for uploading the SignIn form */
  def signIn() = Action {
    implicit request => Ok(views.html.signin(Mappings.signInForm))
  }

  def signout() = Action {
    implicit request => Redirect(routes.HomeController.index()).withNewSession
  }

}