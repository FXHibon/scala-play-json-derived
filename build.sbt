organization := "com.fxhibon"
name := "play-json-derived269"

version := {
  git.gitCurrentTags.value.lastOption match {
    case Some(tag) => tag
    case None => "%s-SNAPSHOT".format(git.gitCurrentBranch.value)
  }
}

scalaVersion := "2.13.4"

libraryDependencies ++= Seq(
  "com.propensive"    %% "magnolia"  % "0.17.0",
  "com.typesafe.play" %% "play-json" % "2.9.1"
)

libraryDependencies += "org.scalameta" %% "munit" % "0.7.19" % Test

testFrameworks += new TestFramework("munit.Framework")
