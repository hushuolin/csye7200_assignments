ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.7"

lazy val root = (project in file("."))
  .settings(
    name := "assignment-api",
    libraryDependencies ++= Seq(
      "com.lihaoyi" %% "requests" % "0.6.9", // HTTP requests library
      "io.circe" %% "circe-core" % "0.14.1", // circe core for JSON parsing
      "io.circe" %% "circe-generic" % "0.14.1", // circe generic for automatic derivation of encoders and decoders
      "io.circe" %% "circe-parser" % "0.14.1", // circe parser for parsing JSON strings
  )
  )
