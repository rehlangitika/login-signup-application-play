package services

import models.{Address, SignedUserData, UserData}
import org.mockito.Mockito._
import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.cache.CacheApi

class CacheUserServiceSpec extends PlaySpec with MockitoSugar {

  val cache = mock[CacheApi]

  "CacheUserService should" should {
    val cache = mock[CacheApi]
    "Get Registered User's data from cache" in {

      val cache = mock[CacheApi]
      val service = new CacheUserService(cache)
      val usersList = List(UserData("Gitika",
        None, "Rehlan", "gitika.rehlan@knoldus.in", "9999747608", 23, Address("New Delhi", "Delhi"), "Female",
        List("Reading"), "gitika", "gitika31", "gitika31", true, false))
      when(cache.get[List[UserData]]("user").toList.flatten).thenReturn(usersList)


      service.getUserData(usersList, SignedUserData("gitika", "gitika31")) mustBe UserData("Gitika",
        None, "Rehlan", "gitika.rehlan@knoldus.in", "9999747608", 23, Address("New Delhi", "Delhi"), "Female",
        List("Reading"), "gitika", "gitika31", "gitika31", true, false)
    }

    "Check if Logged In User has already Registered" in {
      val cache = mock[CacheApi]
      val service = new CacheUserService(cache)
      val usersList = List(UserData("Gitika",
        None, "Rehlan", "gitika.rehlan@knoldus.in", "9999747608", 23, Address("New Delhi", "Delhi"), "Female",
        List("Reading"), "gitika", "gitika31", "gitika31", true, false))

      when(cache.get[List[UserData]]("user").toList.flatten).thenReturn(usersList)
      //when(service.getUserData(usersList,SignedUserData("gitika","gitika31"))).thenReturn(true)
      service.checkLoggedInUser(SignedUserData("gitika", "gitika31")) mustBe true
    }

    "Store UserData in cache" in {
      val cache = mock[CacheApi]
      val service = new CacheUserService(cache)
      val usersList = List(UserData("Gitika",
        None, "Rehlan", "gitika.rehlan@knoldus.in", "9999747608", 23, Address("New Delhi", "Delhi"), "Female",
        List("Reading"), "gitika", "gitika31", "gitika31", true, false))
      when(cache.get[List[UserData]]("user").toList.flatten).thenReturn(usersList)
      service.storeUserData(UserData("Gitika",
        None, "Rehlan", "gitika.rehlan@knoldus.in", "9999747608", 23, Address("New Delhi", "Delhi"), "Female",
        List("Reading"), "gitika", "gitika31", "gitika31", true, false))
    }
  }

}
