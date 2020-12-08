name := "scala-json-derived"

version := "0.1"

scalaVersion := "2.13.4"

libraryDependencies ++= Seq(
  "com.propensive" %% "magnolia" % "0.17.0",
  "com.typesafe.play" %% "play-json" % "2.9.1",
  "org.typelevel" %% "cats-core" % "2.3.0"
)

libraryDependencies += "org.scalameta" %% "munit" % "0.7.19" % Test

testFrameworks += new TestFramework("munit.Framework")
