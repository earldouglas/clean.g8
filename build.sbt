// This build is for this Giter8 template.
// To test the template run `g8` or `g8Test` from the sbt session.
// See https://www.foundweekends.org/giter8/testing.html#Using+the+Giter8Plugin for more details.
lazy val root = (project in file("."))
  .settings(
    name := "clean Template Project",
    Test / test := {
      val _ = (Test / g8Test).toTask("").value
    },
    resolvers += Resolver.url("typesafe", url("https://repo.typesafe.com/typesafe/ivy-releases/"))(Resolver.ivyStylePatterns)
  )
