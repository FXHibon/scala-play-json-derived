lazy val root = project
  .in(file("."))
  .aggregate(
    `play-json-derived-play-27`,
    `play-json-derived-play-28`,
    `play-json-derived-play-29`
  )

val sharedSettings = Seq(
  organization := "com.fxhibon",
  version := {
    git.gitCurrentTags.value.lastOption match {
      case Some(tag) => tag
      case None => "%s-SNAPSHOT".format(git.gitCurrentBranch.value)
    }
  },
  scalaVersion := "2.13.4",
  libraryDependencies += "com.propensive" %% "magnolia" % "0.17.0",
  libraryDependencies += "org.scalameta"  %% "munit"    % "0.7.19" % Test,
  testFrameworks += new TestFramework("munit.Framework")
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
