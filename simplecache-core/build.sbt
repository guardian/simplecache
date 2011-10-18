name := "simplecache-core"

version := "2.1-SNAPSHOT"

organization := "com.gu"

ivyXML :=
    <dependencies>
        <exclude module="log4j"/>
    </dependencies>

resolvers ++= Seq(
  "Guardian GitHub" at "http://guardian.github.com/maven/repo-releases"
)

libraryDependencies ++= Seq(
  "commons-lang" % "commons-lang" % "2.4",
  "commons-codec" % "commons-codec" % "1.3",
  "com.gu" % "management-core" % "3.0.6",
  "com.google.collections" % "google-collections" % "1.0-rc2",
  "org.slf4j" % "slf4j-api" % "1.6.2"
)

libraryDependencies ++= Seq(
  "org.hamcrest" % "hamcrest-all" % "1.1" % "test",
  "org.mockito" % "mockito-all" % "1.8.5" % "test",
  "junit" % "junit" % "4.4" % "test",
  "com.novocode" % "junit-interface" % "0.6" % "test",
  "org.slf4j" % "slf4j-simple" % "1.6.2" % "test"
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