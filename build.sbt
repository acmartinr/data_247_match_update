name := """ConsumerDataBase"""

version := "0.1-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  filters,
  "com.ticketfly" %% "play-liquibase" % "1.0",
  "org.postgresql" % "postgresql" % "42.2.14",

  "org.mybatis" % "mybatis" % "3.3.0",
  "org.mybatis" % "mybatis-guice" % "3.6",

  "com.google.inject.extensions" % "guice-multibindings" % "4.2.3",
  "com.google.inject" % "guice" % "4.2.3",
  "javax.inject" % "javax.inject" % "1",

  "com.fasterxml.jackson.dataformat" % "jackson-dataformat-csv" % "2.9.9",

  "com.typesafe.play" %% "play-mailer" % "5.0.0",

  "org.webjars" % "bootstrap" % "3.3.4",
  "org.webjars" % "angularjs" % "1.3.15",
  "org.webjars" % "angular-ui-bootstrap" % "0.13.0",

  "commons-lang" % "commons-lang" % "2.6",

  "org.apache.httpcomponents" % "httpclient" % "4.2.5",
  "org.apache.httpcomponents" % "httpcore" % "4.2.5",
  "com.google.code.gson" % "gson" % "2.2.3",

  "org.mockito" % "mockito-core" % "1.10.19" % "test",
  "com.stripe" % "stripe-java" % "5.8.0",

  "org.apache.poi" % "poi" % "3.12",
  "org.apache.poi" % "poi-ooxml" % "3.12",

  "com.sendgrid" % "sendgrid-java" % "4.4.1",

  "com.squareup" % "square" % "5.0.0.20200226"
)

// Add app folder as resource directory so that mapper xml files are in the classpath
unmanagedResourceDirectories in Compile <+= baseDirectory( _ / "app" )

// but filter out java and html files that would then also be copied to the classpath
excludeFilter in Compile in unmanagedResources := "*.java" || "*.html"

scriptClasspath += "conf/*"
