name := "simplecache-ehcache"

libraryDependencies ++= Seq(
  "net.sf.ehcache" % "ehcache" % "1.4.1",
  "com.gu" % "option" % "1.2",
  "log4j" % "log4j" % "1.2.14"
)

libraryDependencies ++= Seq(
  "org.hamcrest" % "hamcrest-all" % "1.1" % "test",
  "org.mockito" % "mockito-all" % "1.9.5" % "test",
  "junit" % "junit" % "4.4" % "test",
  "com.novocode" % "junit-interface" % "0.6" % "test"
)
