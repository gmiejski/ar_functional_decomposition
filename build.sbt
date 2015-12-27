name := "AkkaFunctionalDecomposition"

version := "1.0"

scalaVersion := "2.11.4"

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"


val akkaVersion = "2.4.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor"   % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j"   % akkaVersion,
  "com.typesafe.akka" %% "akka-remote"  % akkaVersion,
  "com.typesafe.akka" %% "akka-agent"   % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test"
)
