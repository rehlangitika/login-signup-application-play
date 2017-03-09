package controllers

import javax.inject.Inject

import com.google.inject.name.Named
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller}
import services.UserService

class ProfileController @Inject()(@Named("cache") userService: UserService)(val messagesApi: MessagesApi) extends Controller with I18nSupport {

  /*Action for uploading an image file */
  def upload() = Action(parse.multipartFormData) { implicit request =>
    request.body.file("picture").map { picture =>
      import java.io.File
      val filename = picture.filename
      println(filename)
      val contentType = picture.contentType
      picture.ref.moveTo(new File(s"/tmp/picture/$filename"))
      Redirect(routes.RegisterController.register()).flashing("success" -> "File Uploaded Successfully")
    }.getOrElse {
      Redirect(routes.LoginController.signIn()).flashing("error" -> "File not uploaded")
    }
  }

  /*Action to upload the UploadFile Page*/
  def fileUpload() = Action {
    implicit request => Ok(views.html.uploadfile())
  }

  /*Action for redirecting to the Profile Page after retrieving username from session*/
  def profile() = Action {
    implicit request =>
      request.session.get("registeredUsers").map {
        userName =>
          val regUser = userService.getRegisteredUser(userName)
          Ok(views.html.profile(regUser))
      }.getOrElse(Ok(views.html.user(Mappings.userForm)))
  }


  def checkUserType: String = {
    if (play.Play.application().configuration().getString("Type") == "Admin") {
      s"Admin"
    }
    else s"Normal User"
  }

}
