package fix

import scala.meta._
import scalafix._
import scalafix.testkit._

class Scalafixargonautcirce_Tests
  extends SemanticRewriteSuite(
    SemanticCtx.load(Classpath(AbsolutePath(BuildInfo.inputClassdirectory))),
    AbsolutePath(BuildInfo.inputSourceroot),
    Seq(AbsolutePath(BuildInfo.outputSourceroot))
  ) {
  runAllTests()
}
