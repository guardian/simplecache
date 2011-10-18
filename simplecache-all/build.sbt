name := "simplecache-all"

version := "1.34-SNAPSHOT"

organization := "com.gu"

ivyXML :=
    <dependencies>
        <exclude module="log4j"/>
    </dependencies>

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