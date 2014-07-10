name := "simplecache-all"

version := "2.5"

organization := "com.gu"

scalaVersion := "2.9.2"

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