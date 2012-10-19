organization := "mokc"

name := "prophet"

version := "0.1-SNAPSHOT"

scalaVersion := "2.9.1"

seq(webSettings :_*)

port in container.Configuration := 8080

scalacOptions ++= Seq("-unchecked", "-deprecation")

resolvers ++= Seq("snapshots"     at "http://oss.sonatype.org/content/repositories/snapshots",
                "releases"        at "http://oss.sonatype.org/content/repositories/releases",
                "Spy Repository" at "http://files.couchbase.com/maven2/",
                "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
                )

classpathTypes ~= (_ + "orbit")

libraryDependencies ++= Seq(
  "org.scalatra" % "scalatra" % "2.1.1",
  "org.scalatra" % "scalatra-scalate" % "2.1.1",
  "org.scalatra" % "scalatra-specs2" % "2.1.1" % "test",
  "org.scalatra" % "scalatra-lift-json" % "2.1.1",
  "net.liftweb" %% "lift-json" % "2.5-M1",
  "net.liftweb" %% "lift-json-ext" % "2.5-M1",
  "org.eclipse.jetty"        % "jetty-webapp"           % "8.1.5.v20120716"     % "container",
  "org.eclipse.jetty.orbit"  % "javax.servlet"          % "3.0.0.v201112011016" % "container;provided;test" artifacts (Artifact("javax.servlet", "jar", "jar")),
  "org.slf4j" % "slf4j-simple" % "1.6.1",
  "joda-time" % "joda-time" % "2.1",
  "org.joda" % "joda-convert" % "1.2",
  "spy" % "spymemcached" % "2.8.1",
  "com.typesafe.akka" % "akka-actor" % "2.0.3",
  "org.scalatra" % "scalatra-scalatest" % "2.1.1" % "test"
)
