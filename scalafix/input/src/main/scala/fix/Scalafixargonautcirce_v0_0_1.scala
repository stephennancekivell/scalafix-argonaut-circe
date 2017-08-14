/*
rewrite = "scala:fix.Scalafixargonautcirce_v0_0_1"
*/
package fix

import argonaut._
import Argonaut._

object Scalafixargonautcirce_v0_0_1_Test {
  // Add code that needs fixing here.
  case class User(id: String, first_name: Int, last_name: String)
  implicit val UserDecodeJson: DecodeJson[User] =
    jdecode3L(User.apply)("id", "first_name", "last_name")
  val nonImplicit: DecodeJson[User] =
    jdecode3L(User.apply)("id", "first_name", "last_name")
  val noType = jdecode3L(User.apply)("id", "first_name", "last_name")
  def defUser: DecodeJson[User] =
    jdecode3L(User.apply)("id", "first_name", "last_name")
  def defNoType = jdecode3L(User.apply)("id", "first_name", "last_name")
}
