package controllers

import javax.inject.Inject

import com.google.inject.name.Named
import org.mindrot.jbcrypt.BCrypt
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller}
import services.UserService

class RegisterController @Inject()(@Named("cache") userService: UserService)(val messagesApi: MessagesApi) extends Controller with I18nSupport {

  /*Action when user clicks on the submit button for SignUp and then redirecting to Profile Page*/
  def createUser() = Action { implicit request =>
    println("In Creating user")
    val registerForm = Mappings.userForm.bindFromRequest()
    registerForm.fold(
      hasErrors => {
        Redirect(routes.RegisterController.register()).flashing("error" -> "Fill Details")
      },
      success = {
        implicit registeredUser =>
          //println(checkUserType)
          val passwordHash = BCrypt.hashpw(registeredUser.password, BCrypt.gensalt())
          val newUser = userService.getUser(registeredUser, passwordHash)
          if (checkUserType == "Admin") {
            val adminUser = newUser.copy(isAdmin = true)
            val user = userService.storeUserData(adminUser)
            Redirect(routes.ProfileController.profile()).withSession("registeredUsers" -> adminUser.userName)
          }
          else {
            val normalUser = newUser.copy(isAdmin = false)
            println("ProfileLoggedUser:" + normalUser)
            val user = userService.storeUserData(normalUser)
            Redirect(routes.ProfileController.profile()).withSession("registeredUsers" -> normalUser.userName)
          }
      }
    )
  }

  /*Action for uploading the SignUp form */
  def register() = Action { implicit request =>
    Ok(views.html.user(Mappings.userForm))
  }

  def checkUserType: String = {
    if (play.Play.application().configuration().getString("Type") == "Admin") {
      s"Admin"
    }
    else s"Normal User"
  }

}
