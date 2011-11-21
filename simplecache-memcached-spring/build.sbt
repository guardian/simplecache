name := "simplecache-spring-memcached"

version := "2.2-SNAPSHOT"

organization := "com.gu"

resolvers ++= Seq(
  "Guardian GitHub" at "http://guardian.github.com/maven/repo-releases",
  "Spy Memcached" at "http://files.couchbase.com/maven2"
)

libraryDependencies ++= Seq(
  "com.gu" % "option" % "1.2",
  "spy" % "spymemcached" % "2.7.3",
  "org.springframework" % "spring-beans" % "3.0.0.RELEASE",
  "log4j" % "log4j" % "1.2.14"
)

libraryDependencies ++= Seq(
  "javax.servlet" % "servlet-api" % "2.5" % "provided"
)

libraryDependencies ++= Seq(
  "org.hamcrest" % "hamcrest-all" % "1.1" % "test",
  "org.mockito" % "mockito-all" % "1.8.5" % "test",
  "junit" % "junit" % "4.4" % "test",
  "com.novocode" % "junit-interface" % "0.6" % "test"
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

crossPaths := false