ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := "aoc",
    libraryDependencies ++= Seq("com.outr" %% "hasher" % "1.2.2",
"org.scala-lang.modules" %% "scala-parser-combinators" % "2.4.0",
"org.scala-lang.modules" %% "scala-parallel-collections" % "1.0.4")
  )
