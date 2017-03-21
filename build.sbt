name := """narrative_analytics"""

organization := "com.finfrock"

version := "0.0.1"

scalaVersion := "2.11.7"

val akkaVersion = "2.4.4"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-agent" % akkaVersion,
  "com.typesafe.akka" %% "akka-remote" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion,
  "junit" % "junit" % "4.11" % Test,
  "com.novocode" % "junit-interface" % "0.11" % Test,
  "log4j" % "log4j" % "1.2.17",
  "joda-time" % "joda-time" % "2.9.4",
  "org.joda" % "joda-convert" % "1.7",
  "com.datastax.cassandra" % "cassandra-driver-core" % "3.1.3"
)

javaOptions in run ++= Seq(
  "-Xms128m", "-Xmx1024m", "-Djava.library.path=./target/native")

unmanagedResourceDirectories in Compile += baseDirectory.value / "config"

scalacOptions ++= Seq("-Xmax-classfile-name","78")

enablePlugins(JavaAppPackaging)

mainClass in Compile := Some("com.finfrock.narrative_analytics.Bootable")

scriptClasspath := Seq("../config/") ++ scriptClasspath.value

fork in run := false
