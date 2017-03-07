
package services

import javax.inject.Inject

import models.{Address, SignedUserData, UserData}
import play.api.cache.CacheApi


class CacheUserService @Inject()(cache: CacheApi) extends UserService{

  def checkLoggedInUser(newUser: SignedUserData): Boolean = {
    cache.get[UserData](newUser.userName) match {
      case Some(data) => data.password.equals(newUser.password)
      case None => false
    }
  }

  def getRegisteredUser(userName: String): UserData = {
    val user = cache.get[UserData](userName)
    user match {
      case Some(data) if userName.equals(data.userName) => data
      case None => UserData("", None, "", "", "", 0, Address("", ""), "", List(""), "", "", "")
    }
  }

  def storeUserData(registeredUser: UserData) = {
    cache.set(registeredUser.userName, registeredUser)
    registeredUser
  }

}