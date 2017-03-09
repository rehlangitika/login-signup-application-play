package models

case class UserData(firstName: String, middleName: Option[String], lastName: String, email: String, contact: String,
                    age: Int, address: Address, gender: String, hobbies: List[String], userName: String,
                    password: String, confirmPassword: String, isAdmin: Boolean, suspend: Boolean)

/*
val map = mutable.Map[String, String]()

map("firstName") = obj.firstName


cache.set(obj.userName, map.toMap)
*/
