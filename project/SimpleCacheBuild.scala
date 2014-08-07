import sbt.Keys._
import sbt._

object SimpleCacheBuild extends Build {

  lazy val all = Project("simplecache-all", file(".")) aggregate (
      core, ehcache, hibernate, memcached, memcached_spring
    ) dependsOn (
      core, ehcache, hibernate, memcached, memcached_spring
    ) settings(commonSettings: _*)

  lazy val commonSettings = Seq(
    organization := "com.gu",
    version := "2.5.2",

    scalaVersion := "2.9.2",
    crossScalaVersions := Seq("2.9.2", "2.10.4"),
    crossPaths := true,

    javacOptions in (Compile, compile) ++= Seq("-source", "1.6", "-target", "1.6", "-Xlint:deprecation"),
    javacOptions in doc := Seq("-source", "1.6"),

    resolvers += "Guardian GitHub" at "http://guardian.github.com/maven/repo-releases",

    publishTo := {
      val publishType = if (version.value.endsWith("SNAPSHOT")) "snapshots" else "releases"
      Some(
        Resolver.file(
          "guardian github " + publishType,
          file(System.getProperty("user.home") + "/guardian.github.com/maven/repo-" + publishType)
        )
      )
    },

     maxErrors := 20
  )

  lazy val core = Project("simplecache-core", file("simplecache-core")).settings(commonSettings: _*)

  lazy val ehcache = Project("simplecache-ehcached", file("simplecache-ehcache")) dependsOn core settings(commonSettings: _*)
  lazy val hibernate = Project("simplecache-hibernate", file("simplecache-hibernate")) dependsOn core settings(commonSettings: _*)
  lazy val memcached = Project("simplecache-memcached", file("simplecache-memcached")) dependsOn core settings(commonSettings: _*)

  lazy val memcached_spring = Project("simplecache-memcached-spring", file("simplecache-memcached-spring")) dependsOn memcached settings(commonSettings: _*)
}