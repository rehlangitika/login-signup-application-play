package services

import models.{SignedUserData, UserData}

trait UserService {

  def storeUserData(registeredUser: UserData): UserData

  def getRegisteredUser(userName: String): UserData

  def checkLoggedInUser(newUser: SignedUserData): Boolean

}
