lazy val V = _root_.scalafix.Versions
// Use a scala version supported by scalafix.
scalaVersion in ThisBuild := V.scala211

val circeVersion = "0.8.0"
val circeDeps = Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

lazy val rewrites = project.settings(
  libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % V.version
)

lazy val input = project.settings(
  scalafixSourceroot := sourceDirectory.in(Compile).value,
  libraryDependencies += "io.argonaut" %% "argonaut" % "6.1a"
)

lazy val output = project.settings(
  libraryDependencies ++= circeDeps
)

lazy val tests = project
  .settings(
    libraryDependencies += "ch.epfl.scala" % "scalafix-testkit" % V.version % Test cross CrossVersion.full,
    buildInfoPackage := "fix",
    buildInfoKeys := Seq[BuildInfoKey](
      "inputSourceroot" ->
        sourceDirectory.in(input, Compile).value,
      "outputSourceroot" ->
        sourceDirectory.in(output, Compile).value,
      "inputClassdirectory" ->
        classDirectory.in(input, Compile).value
    )
  )
  .dependsOn(input, rewrites)
  .enablePlugins(BuildInfoPlugin)
