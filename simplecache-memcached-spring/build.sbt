name := "simplecache-spring-memcached"

resolvers += "Spy Memcached" at "http://files.couchbase.com/maven2"

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
  "org.mockito" % "mockito-all" % "1.9.5" % "test",
  "junit" % "junit" % "4.4" % "test",
  "com.novocode" % "junit-interface" % "0.6" % "test"
)