name := "hello"

version := "1.0"

scalaVersion := "2.11.4"

libraryDependencies ++= {
  val akkaV = "2.3.6"
  val sprayV = "1.3.2"
  Seq(
    "io.spray"            %%  "spray-can"     % sprayV,
    "io.spray"            %%  "spray-routing" % sprayV,
    "io.spray"            %%  "spray-testkit" % sprayV  % "test",
    "com.typesafe.akka"   %%  "akka-actor"    % akkaV,
    "com.typesafe.akka"   %%  "akka-testkit"  % akkaV   % "test",
    "org.json4s" %% "json4s-native" % "3.2.11",
    "org.scalatest" % "scalatest_2.11" % "2.2.1" % "test"
  )
}
    