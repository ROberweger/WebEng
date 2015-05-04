name := """we-lab3-group49"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  javaCore,
  javaJpa,
  "org.hibernate" % "hibernate-entitymanager" % "4.3.1.Final"
  "com.google.code.gson" % "gson" % "2.2"
)

templateImport += "scala.collection._"
templateImport += "at.ac.tuwien.big.we15.lab2.api._"