ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := "aoc",
    libraryDependencies ++= Seq("com.outr" %% "hasher" % "1.2.2")
  )
