ThisBuild / scalaVersion := "3.5.2"

ThisBuild / Compile / run / fork := true

// Scalafix settings
inThisBuild(
  List(
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    scalacOptions += "-Wunused:imports"
  )
)

lazy val root = project
  .in(file("."))
  .dependsOn(l4_runners)
  .aggregate(l4_runners, l3_drivers, l2_use_cases, l1_domain)

lazy val l4_runners = project
  .in(file("modules/l4-runners"))
  .settings(
    libraryDependencies += "org.http4s" %% "http4s-ember-client" % "1.0.0-M44",
    libraryDependencies += "org.http4s" %% "http4s-ember-server" % "1.0.0-M44",
    libraryDependencies += "org.http4s" %% "http4s-dsl" % "1.0.0-M44",
    libraryDependencies += "org.typelevel" %% "log4cats-slf4j" % "2.7.0",
    libraryDependencies += "org.http4s" %% "http4s-circe" % "1.0.0-M44"
  )
  .dependsOn(l3_drivers, l2_use_cases, l1_domain)

lazy val l3_drivers = project
  .in(file("modules/l3-drivers"))
  .settings(
    libraryDependencies += "com.h2database" % "h2" % "2.3.232",
    libraryDependencies += "org.typelevel" %% "munit-cats-effect" % "2.0.0" % Test
  )
  .dependsOn(l2_use_cases, l1_domain)

lazy val l2_use_cases = project
  .in(file("modules/l2-use-cases"))
  .settings(
    libraryDependencies += "org.typelevel" %% "cats-effect" % "3.5.7"
  )
  .dependsOn(l1_domain)

lazy val l1_domain = project
  .in(file("modules/l1-domain"))
