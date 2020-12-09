lazy val root = project
  .in(file("."))
  .aggregate(
    `play-json-derived-play-27`,
    `play-json-derived-play-28`,
    `play-json-derived-play-29`
  )

val scala212 = "2.12.12"
val scala213 = "2.13.4"
val sharedSettings = Seq(
  organization := "com.fxhibon",
  version := {
    git.gitCurrentTags.value.lastOption match {
      case Some(tag) => tag
      case None => "%s-SNAPSHOT".format(git.gitCurrentBranch.value)
    }
  },
  scalaVersion := scala213,
  crossScalaVersions := Seq(scala212, scala213),
  libraryDependencies += "com.propensive" %% "magnolia" % "0.17.0",
  libraryDependencies += "org.scalameta"  %% "munit"    % "0.7.19" % Test,
  testFrameworks += new TestFramework("munit.Framework"),
  parallelExecution in Test := sys.env.getOrElse("SBT_PARALLEL_EXECUTION", "true").toBoolean,
  scalacOptions ++= Seq("-deprecation", "-feature", "-language:higherKinds"),
  scalacOptions ++= {
    sys.env.getOrElse("SBT_FATAL_WARNINGS", "true").toBoolean match {
      case true => Seq("-Xfatal-warnings")
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

lazy val `play-json-derived-play-27` = project
  .settings(sharedDirs: _*)
  .settings(sharedSettings)
  .settings(
    libraryDependencies += "com.typesafe.play" %% "play-json" % "2.7.4"
  )

lazy val `play-json-derived-play-28` = project
  .settings(sharedDirs: _*)
  .settings(sharedSettings)
  .settings(
    libraryDependencies += "com.typesafe.play" %% "play-json" % "2.8.1"
  )

lazy val `play-json-derived-play-29` = project
  .settings(sharedDirs: _*)
  .settings(sharedSettings)
  .settings(
    libraryDependencies += "com.typesafe.play" %% "play-json" % "2.9.1"
  )
