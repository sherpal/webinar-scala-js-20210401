name := "Full Stack Scala"
organization := "B12"

val commonSettings = List(
  scalaVersion := "2.13.5",
  scalacOptions ++= List(
    "-Ymacro-annotations"
  ),
  libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value % Provided
)

val circeVersion = "0.14.0-M3"

lazy val shared = crossProject(JSPlatform, JVMPlatform)
  .in(file("./shared"))
  .settings(
    commonSettings,
    libraryDependencies ++= List(
      "io.circe" %%% "circe-core",
      "io.circe" %%% "circe-generic",
      "io.circe" %%% "circe-parser"
    ).map(_ % circeVersion) ++ List(
      "org.scalameta" %%% "munit" % "0.7.22" % Test,
      "com.propensive" %%% "magnolia" % "0.17.0"
    )
  )

lazy val server = project
  .in(file("./server"))
  .enablePlugins(PlayScala)
  .settings(
    commonSettings,
    libraryDependencies += guice
  )
  .dependsOn(shared.jvm)

lazy val frontend = project
  .in(file("./frontend"))
  .enablePlugins(ScalaJSPlugin)
  .enablePlugins(ScalablyTypedConverterExternalNpmPlugin)
  .settings(
    commonSettings,
    scalaJSUseMainModuleInitializer := true,
    externalNpm := {
      scala.sys.process.Process("npm", baseDirectory.value).!
      baseDirectory.value
    },
    scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.ESModule) },
    libraryDependencies += "com.raquo" %%% "laminar" % "0.12.1"
  )
  .dependsOn(shared.js)
