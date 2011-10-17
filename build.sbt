name := "simplecache"

version := "1.34-SNAPSHOT"

organization := "com.gu"

scalaVersion := "2.9.1"

resolvers ++= Seq(
  "Guardian GitHub" at "http://guardian.github.com/maven/repo-releases"
)

libraryDependencies ++= Seq(
  "spy" % "memcached" % "2.4.2",
  "commons-lang" % "commons-lang" % "2.4",
  "commons-codec" % "commons-codec" % "1.3",
  "net.sf.ehcache" % "ehcache" % "1.4.1",
  "org.hibernate" % "hibernate-core" % "3.3.2.GA",
  "com.gu" % "management-core" % "3.0.6",
  "org.springframework" % "spring-beans" % "3.0.0.RELEASE",
  "com.google.collections" % "google-collections" % "1.0-rc2",
  "log4j" % "log4j" % "1.2.14"
//  "org.slf4j" % "slf4j-api" % "1.6.1"
)

libraryDependencies ++= Seq(
  "javax.servlet" % "servlet-api" % "2.5" % "provided"
)

libraryDependencies ++= Seq(
  "org.hamcrest" % "hamcrest-all" % "1.1" % "test",
  "org.mockito" % "mockito-all" % "1.8.5" % "test",
  "junit" % "junit" % "4.4" % "test",
  "com.novocode" % "junit-interface" % "0.6" % "test",
  "org.slf4j" % "slf4j-simple" % "1.6.1" % "test"
)

publishTo <<= (version) { version: String =>
    val publishType = if (version.endsWith("SNAPSHOT")) "snapshots" else "releases"
    Some(
        Resolver.file(
            "guardian github " + publishType,
            file(System.getProperty("user.home") + "/guardian.github.com/maven/repo-" + publishType)
        )
    )
}

maxErrors := 20

javacOptions ++= Seq("-source", "1.6", "-target", "1.6", "-Xlint:deprecation")

scalacOptions += "-deprecation"
