import sbt._

object Dependencies {
  // Versions
  val akkaV       = "2.5.3"
  val akkaHttpV   = "10.0.9"
  val scalaTestV  = "3.0.1"
  val slickVersion = "3.2.0-M2"

  // Libraries
  val akkaActor = "com.typesafe.akka" %% "akka-actor" % akkaV
  val akkaStream = "com.typesafe.akka" %% "akka-stream" % akkaV
  val akkaPersistance = "com.typesafe.akka" %% "akka-persistence" % akkaV
  val akkaPersistanceQuery = "com.typesafe.akka" %% "akka-persistence-query" % akkaV
  val akkaTestkit = "com.typesafe.akka" %% "akka-testkit" % akkaV
  val akkaHTTP = "com.typesafe.akka" %% "akka-http" % akkaHttpV
  val akkaHTTPSprayJson = "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV
  val akkaHTTPTestKid = "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV

  val slick = "com.typesafe.slick" %% "slick" % slickVersion
  val postgresql =  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41"
  val flywayCore = "org.flywaydb" % "flyway-core" % "3.2.1"

  val sprayJson = "io.spray" %%  "spray-json" % "1.3.3"

  val scalaTest = "org.scalatest" %% "scalatest" % scalaTestV % "test"


  // akka 2.5.x
  val AkkainMemeor = "com.github.dnvriend" %% "akka-persistence-inmemory" % "2.5.1.1"

  // leveldb
  val leveldb = "org.iq80.leveldb"            % "leveldb"          % "0.9"
  val leveldbjni = "org.fusesource.leveldbjni"   % "leveldbjni-all"   % "1.8"

  // Projects
  val domainDeps =
    Seq(akkaActor, akkaPersistance, akkaTestkit, scalaTest, AkkainMemeor, leveldb)

  val frontendDeps =
    Seq(akkaActor, akkaPersistance, akkaStream, akkaHTTP, akkaHTTPSprayJson, akkaHTTPTestKid, akkaTestkit,
      sprayJson, scalaTest, AkkainMemeor, leveldb)

  val projectionsDeps =
    Seq(scalaTest, akkaActor, akkaPersistance, akkaStream, akkaPersistanceQuery, akkaTestkit, AkkainMemeor, leveldb)

  val serverDeps =
    Seq(akkaActor, akkaTestkit, scalaTest, AkkainMemeor, leveldb)

  val utilsDeps =
    Seq(scalaTest, akkaHTTP, akkaHTTPSprayJson, akkaHTTPTestKid, AkkainMemeor, leveldb)
}