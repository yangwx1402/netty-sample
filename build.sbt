import sbt.Keys._

logLevel := Level.Warn
val project_version = "1.0"
val scala_version = "2.11.8"
val kyro_version = "2.24.0"
val netty_version = "4.1.13.Final"
val jackson_version = "1.9.13"
val codePro = Seq("-unchecked", "-deprecation", "-encoding", "utf8")
val resources = Seq("Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "Maven Repository" at "http://repo1.maven.org/maven2/",
  "maven-restlet" at "http://maven.restlet.org",
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/")

val netty_example = Project("netty-example", file("netty-example")).settings(
  name := "netty-example",
  version := project_version,
  resolvers ++= resources,
  scalaVersion := scala_version,
  scalacOptions := codePro,
  libraryDependencies += "com.esotericsoftware.kryo" % "kryo" % kyro_version,
  libraryDependencies += "io.netty" % "netty-all" % netty_version,
  libraryDependencies += "org.codehaus.jackson" % "jackson-core-asl" % jackson_version,
  libraryDependencies += "org.codehaus.jackson" % "jackson-mapper-asl" % jackson_version
)

val root = Project("netty-sample", file(".")).settings(publish := {}).settings(publishArtifact := false)
  .settings(
    organization in ThisBuild := "com.young.netty",
    name := "netty-sample",
    resolvers ++= resources,
    version := project_version,
    scalaVersion := scala_version,
    scalacOptions := codePro
  ).aggregate(netty_example)