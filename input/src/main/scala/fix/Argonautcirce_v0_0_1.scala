/* ONLY
rewrite = "scala:fix.Argonautcirce_v0_0_1"
 */
package fix

import argonaut._
import Argonaut._

object Argonautcirce_v0_0_1_Test {
  // Add code that needs fixing here.
  case class User(id: String, first_name: Int, last_name: String)
  implicit val UserDecodeJson: DecodeJson[User] =
    jdecode3L(User.apply)("id", "first_name", "last_name")
  val nonImplicit: DecodeJson[User] =
    jdecode3L(User.apply)("id", "first_name", "last_name")
}
