package fix

import io.circe._, io.circe.parser._
import io.circe.syntax._

object Scalafixargonautcirce_v0_0_1_Test {
  // Add code that needs fixing here.
  case class User(id: String, first_name: Int, last_name: String)
  implicit val UserDecodeJson: Decoder[User] = Decoder.forProduct3("id", "first_name", "last_name")(User.apply)
  val nonImplicit: Decoder[User] = Decoder.forProduct3("id", "first_name", "last_name")(User.apply)
  val noType = Decoder.forProduct3("id", "first_name", "last_name")(User.apply)
  def defUser: Decoder[User] = Decoder.forProduct3("id", "first_name", "last_name")(User.apply)
  def defNoType = Decoder.forProduct3("id", "first_name", "last_name")(User.apply)
}
