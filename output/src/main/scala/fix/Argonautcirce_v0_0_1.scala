package fix

import io.circe._, io.circe.parser._
import io.circe.syntax._

object Argonautcirce_v0_0_1_Test {
  // Add code that needs fixing here.
  case class User(id: String, first_name: Int, last_name: String)
  implicit val UserDecodeJson: Decoder[User] = Decoder.forProduct3("id", "first_name", "last_name")(User.apply)
}