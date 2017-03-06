import javax.inject.Singleton

import play.api.http.HttpErrorHandler
import play.api.mvc.RequestHeader

import scala.concurrent.Future
import play.api.mvc.Results._

@Singleton
class ErrorHandler extends HttpErrorHandler {

  def onClientError(request: RequestHeader, statusCode: Int, message: String) = {
    statusCode match {
      case 400 => Future.successful(
        Status(statusCode)("A client Error Occurred -> Bad Request " + message)
      )
      case 401 => Future.successful(
        Status(statusCode)("A client Error Occurred -> Unauthorized " + message)
      )
      case 403 => Future.successful(
        Status(statusCode)("A client Error Occurred -> Forbidden " + message)
      )
      case 404 => Future.successful(
        Status(statusCode)("A client Error Occurred -> Page Not Found " + message)
      )

    }

  }

  def onServerError(request: RequestHeader, exception: Throwable) = {
    Future.successful(InternalServerError("A server error occurred: "
      + exception.getMessage)
    )
  }

}

