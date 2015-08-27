name := "simplecache-core"

libraryDependencies ++= Seq(
  "commons-lang" % "commons-lang" % "2.4",
  "commons-codec" % "commons-codec" % "1.3",
  "com.gu" %% "management" % "5.35",
  "com.gu" % "option" % "1.2",
  "com.google.collections" % "google-collections" % "1.0-rc2",
  "log4j" % "log4j" % "1.2.14"
)

libraryDependencies ++= Seq(
  "org.hamcrest" % "hamcrest-all" % "1.1" % "test",
  "org.mockito" % "mockito-all" % "1.9.5" % "test",
  "junit" % "junit" % "4.4" % "test",
  "com.novocode" % "junit-interface" % "0.6" % "test"
)
