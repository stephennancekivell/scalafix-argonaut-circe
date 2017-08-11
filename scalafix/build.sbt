name := """scalafix-argonaut-circe"""
// Use a scala version supported by scalafix.
scalaVersion in ThisBuild := org.scalameta.BuildInfo.supportedScalaVersions.last

val circeVersion = "0.8.0"
val circeDeps = Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

lazy val rewrites = project.settings(
  libraryDependencies ++= Seq("ch.epfl.scala" %% "scalafix-core" % "0.5.0-M2") ++ circeDeps
)

lazy val input = project.settings(
  scalametaSourceroot := sourceDirectory.in(Compile).value,
  libraryDependencies += "io.argonaut" %% "argonaut" % "6.1a"
)


lazy val output = project.settings(
  libraryDependencies ++= circeDeps
)

lazy val tests = project
  .settings(
    libraryDependencies += "ch.epfl.scala" % "scalafix-testkit" % "0.5.0-M2" % Test cross CrossVersion.full,
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
