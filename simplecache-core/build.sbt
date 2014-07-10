name := "simplecache-core"

version := "2.5"

scalaVersion := "2.9.2"

organization := "com.gu"

resolvers ++= Seq(
  "Guardian GitHub" at "http://guardian.github.com/maven/repo-releases"
)

libraryDependencies ++= Seq(
  "commons-lang" % "commons-lang" % "2.4",
  "commons-codec" % "commons-codec" % "1.3",
  "com.gu" %% "management" % "5.27",
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

javacOptions in (Compile, compile) ++= Seq("-source", "1.6", "-target", "1.6", "-Xlint:deprecation")

javacOptions in doc := Seq("-source", "1.6")

crossPaths := true