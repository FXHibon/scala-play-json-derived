lazy val root = project
  .in(file("."))
  .settings(
    publish / skip := true
  )
  .aggregate(
    `play-json-derived-play-26`,
    `play-json-derived-play-27`,
    `play-json-derived-play-28`,
    `play-json-derived-play-29`
  )

val scala212 = "2.12.13"
val scala213 = "2.13.5"
val sharedSettings = Seq(
  organization := "com.github.fxhibon",
  scalaVersion := scala213,
  crossScalaVersions := Seq(scala212, scala213),
  libraryDependencies += "com.propensive" %% "magnolia" % "0.17.0",
  libraryDependencies += "org.scalameta"  %% "munit"    % "0.7.23" % Test,
  testFrameworks += new TestFramework("munit.Framework"),
  Test / parallelExecution := sys.env.getOrElse(key = "SBT_PARALLEL_EXECUTION", default = "true").toBoolean,
  scalacOptions ++= Seq("-deprecation", "-feature", "-language:higherKinds"),
  scalacOptions ++= {
    sys.env.getOrElse(key = "SBT_FATAL_WARNINGS", default = "true").toBoolean match {
      case true => Seq("-Xfatal-warnings", "-Ywarn-unused:imports")
      case false => Seq.empty
    }
  }
)

val sharedDirs: Seq[Def.SettingsDefinition] = {
  val sharedSrcDirectory = Def.settingDyn {
    Def.setting((root / baseDirectory).value / "shared")
  }
  Seq(
    Compile / unmanagedSourceDirectories += sharedSrcDirectory.value / "src" / "main" / "scala",
    Test / unmanagedSourceDirectories += sharedSrcDirectory.value / "src" / "test" / "scala"
  )
}

lazy val `play-json-derived-play-26` = project
  .settings(sharedDirs: _*)
  .settings(sharedSettings)
  .settings(
    name := "play-json-derived-play-26",
    libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.14",
    scalaVersion := scala212,
    crossScalaVersions := Seq(scala212)
  )

lazy val `play-json-derived-play-27` = project
  .settings(sharedDirs: _*)
  .settings(sharedSettings)
  .settings(
    name := "play-json-derived-play-27",
    libraryDependencies += "com.typesafe.play" %% "play-json" % "2.7.4"
  )

lazy val `play-json-derived-play-28` = project
  .settings(sharedDirs: _*)
  .settings(sharedSettings)
  .settings(
    name := "play-json-derived-play-28",
    libraryDependencies += "com.typesafe.play" %% "play-json" % "2.8.1"
  )

lazy val `play-json-derived-play-29` = project
  .settings(sharedDirs: _*)
  .settings(sharedSettings)
  .settings(
    name := "play-json-derived-play-29",
    libraryDependencies += "com.typesafe.play" %% "play-json" % "2.9.2"
  )
