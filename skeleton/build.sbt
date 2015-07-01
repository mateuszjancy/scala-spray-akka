name := "skeleton"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= {
  val akkaV = "2.3.6"
  val sprayV = "1.3.2"
  Seq(
    "io.spray" %% "spray-can" % sprayV,
    "io.spray" %% "spray-routing" % sprayV,
    "io.spray" %% "spray-json" % "1.3.2",
    "io.spray" %% "spray-testkit" % sprayV % "test",
    //"com.wandoulabs.akka" %% "spray-websocket" % "0.1.4",
    "com.zaxxer" % "HikariCP" % "2.3.8",
    "com.typesafe.slick" %% "slick" % "3.0.0",
    "org.slf4j" % "slf4j-api" % "1.7.12",
    "ch.qos.logback" % "logback-classic" % "1.1.3",
    "mysql" % "mysql-connector-java" % "5.1.35",
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-testkit" % akkaV % "test",
    "org.json4s" %% "json4s-native" % "3.2.11",
    "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test"
  )
}