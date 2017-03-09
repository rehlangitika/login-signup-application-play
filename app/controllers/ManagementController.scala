package controllers

import javax.inject.Inject

import com.google.inject.name.Named
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller}
import services.UserService

class ManagementController @Inject()(@Named("cache") userService: UserService)(val messagesApi: MessagesApi) extends Controller with I18nSupport {

  def management() = Action {
    implicit request =>
      val usersList = userService.getListOfUsers()
      Ok(views.html.management(usersList))
  }

  def suspend(userName: String) = Action { implicit request =>
    val user = userService.getRegisteredUser(userName)
    val suspendedUser = user.copy(suspend = true)
    userService.storeUserData(suspendedUser)
    Redirect(routes.ManagementController.management()).flashing("suspend" -> "suspend")
  }

  def resume(userName: String) = Action { implicit request =>
    val user = userService.getRegisteredUser(userName)
    val resumedUser = user.copy(suspend = false)
    userService.storeUserData(resumedUser)
    Redirect(routes.ManagementController.management()).flashing("resume" -> "resume")
  }

}
