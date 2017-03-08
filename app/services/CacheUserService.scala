
package services

import javax.inject.Inject

import models.{Address, SignedUserData, UserData}
import org.mindrot.jbcrypt.BCrypt
import play.api.cache.CacheApi

class CacheUserService @Inject()(cache: CacheApi) extends UserService{

  def checkLoggedInUser(newUser: SignedUserData): Boolean = {
    cache.get[UserData](newUser.userName) match {
      case Some(data) => BCrypt.checkpw(newUser.password,data.password)
      case None => false
    }
  }

  def getRegisteredUser(userName: String): UserData = {
    val user = cache.get[UserData](userName)
    user match {
      case Some(data) if userName.equals(data.userName) => data
      case None => UserData("", None, "", "", "", 0, Address("", ""), "", List(""), "", "", "",false,false)
    }
  }

  def storeUserData(registeredUser: UserData) = {
    cache.set(registeredUser.userName, registeredUser)
    registeredUser
  }

  def getUser(registeredUser: UserData, passwordHash: String) = {
    UserData(registeredUser.firstName, registeredUser.middleName, registeredUser.lastName,
      registeredUser.email,registeredUser.contact,registeredUser.age,
      registeredUser.address,registeredUser.gender,registeredUser.hobbies,
      registeredUser.userName, passwordHash,passwordHash,registeredUser.isAdmin,registeredUser.suspend)
  }

}
