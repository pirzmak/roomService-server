import Dependencies._

name := "roomService"

val akkaV       = "2.5.3"
val akkaHttpV   = "10.0.9"
val scalaTestV  = "3.0.1"
val slickVersion = "3.2.0-M2"
val circeV = "0.6.1"

lazy val commonSettings = Seq(
  organization := "me.server",
  version := "1.0.0",
  scalaVersion := "2.12.1"
)

resolvers += Resolver.jcenterRepo

lazy val root = (project in file("."))
  .aggregate(server,projections,projections_api,frontend,domain,domain_api,utils)

lazy val server = (project in file("server"))
  .settings(
    commonSettings,
    name := "server",
    libraryDependencies ++= serverDeps

  )
  .dependsOn(frontend,domain,projections,utils)

lazy val frontend = (project in file("frontend"))
  .settings(
    commonSettings,
    name := "frontend",
    libraryDependencies ++= frontendDeps
  )
  .dependsOn(utils,domain,projections)

lazy val clientRef = LocalProject("client")

lazy val projections = (project in file("projections"))
  .settings(
    commonSettings,
    name := "projections",
    libraryDependencies ++= projectionsDeps
  )
  .dependsOn(domain,domain_api,projections_api,utils)

lazy val projections_api = (project in file("projections_api"))
  .settings(
    commonSettings,
    name := "projections_api",
    libraryDependencies ++= projectionsDeps
  )
  .dependsOn(domain_api,utils)

lazy val domain = (project in file("domain"))
  .settings(
    commonSettings,
    name := "domain",
    libraryDependencies ++= domainDeps
  )
  .dependsOn(domain_api, projections_api, utils)

lazy val domain_api = (project in file("domain_api"))
  .settings(
    commonSettings,
    name := "domain_api",
    libraryDependencies ++= domainDeps
  )
  .dependsOn(utils)

lazy val utils = (project in file("utils"))
  .settings(
    commonSettings,
    name := "utils",
    libraryDependencies ++= utilsDeps
  )


