
package services

import javax.inject.Inject

import models.{Address, SignedUserData, UserData}
import org.mindrot.jbcrypt.BCrypt
import play.api.cache.CacheApi

class CacheUserService @Inject()(cache: CacheApi) extends UserService {

  val listOfUsers = List(UserData("Gitika", None, "Rehlan", "gitika.rehlan@knoldus.in",
  "9999747608",23,Address("New Delhi","Delhi"),"Female",List("Reading"),"gitika","gitika31","gitika31",true,false))

  cache.set("user",listOfUsers)

  def checkLoggedInUser(newUser: SignedUserData): Boolean = {
    val usersList = cache.get[List[UserData]]("user").toList.flatten
    getUserData(usersList,newUser)
  }

  def getRegisteredUser(userName: String): UserData = {
    val user = cache.get[List[UserData]]("user").toList.flatten
    println("User List : "+user)
    val regUser: Option[UserData] = user.find(x => x.userName.equals(userName))
    regUser match {
      case Some(data) => data
      case None => throw new Exception("No User Exception!!")
    }
  }

  def storeUserData(registeredUser: UserData): UserData = {
    println("Before storing")
    val usersList = cache.get[List[UserData]]("user").toList.flatten
    println("Storing user : "+usersList)
    cache.remove("user")
    cache.set("user", usersList:+registeredUser)
    println(s"Stored data : ${usersList:+registeredUser}")
    registeredUser
  }

  def getUser(registeredUser: UserData, passwordHash: String) = {
    registeredUser.copy(password = passwordHash, confirmPassword = passwordHash)
  }

  def getUserData(usersList: List[UserData], signedUser: SignedUserData): Boolean = {
   usersList match {
     case Nil => false
     case head::tail if head.userName.equals(signedUser.userName) && !head.suspend => {
       BCrypt.checkpw(signedUser.password,head.password)
     }
     case head::tail => getUserData(tail,signedUser)
   }
  }

  def getListOfUsers() : List[UserData] = {
    println("Retrieving List of Users")
   cache.get[List[UserData]]("user").toList.flatten
  }


}
