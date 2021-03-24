ThisBuild / version := {
  git.gitCurrentTags.value.lastOption match {
    case Some(tag) => tag
    case None => "%s-SNAPSHOT".format(git.gitCurrentBranch.value)
  }
}

ThisBuild / credentials += Credentials(
  realm = "Sonatype Nexus Repository Manager",
  host = "oss.sonatype.org",
  userName = sys.env.getOrElse("SONATYPE_USERNAME", ""),
  passwd = sys.env.getOrElse("SONATYPE_PASSWORD", "")
)

ThisBuild / licenses := Seq("MIT" -> url("https://opensource.org/licenses/mit-license.php"))

ThisBuild / publishMavenStyle := true

ThisBuild / sonatypeProjectHosting := {
  import xerial.sbt.Sonatype._
  Some(GitHubHosting("FXHIBON", "scala-play-json-derived", "fxhibon@protonmail.com"))
}

sonatypeProfileName := "com.github.fxhibon"

ThisBuild / homepage := Some(url("https://github.com/FXHibon/scala-play-json-derived"))

ThisBuild / developers := List(
  Developer(
    id = "FXHibon",
    name = "FXHibon",
    email = "fxhibon@protonmail.com",
    url = url("https://github.com/FXHibon")
  )
)

ThisBuild / publishTo := sonatypePublishToBundle.value

ThisBuild / versionScheme := Some("early-semver")
