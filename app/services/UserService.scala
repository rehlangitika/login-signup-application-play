package services

import models.{SignedUserData, UserData}

trait UserService {

  def storeUserData(registeredUser: UserData): UserData

  def getRegisteredUser(userName: String): UserData

  def checkLoggedInUser(newUser: SignedUserData): Boolean

  def getUser(registeredUser: UserData, passwordHash: String): UserData

  def getListOfUsers(): List[UserData]
}
