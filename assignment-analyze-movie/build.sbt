name := "MovieDatabase"

version := "1.0"

scalaVersion := "2.13.7"

Compile / doc / scalacOptions ++= Seq("-Vimplicits", "-deprecation", "-Ywarn-dead-code", "-Ywarn-value-discard", "-Ywarn-unused")

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.2" % "test"