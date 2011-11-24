import sbt._

object SimpleCacheBuild extends Build {

  lazy val all = Project("simplecache-all", file(".")) aggregate (
      core, ehcache, hibernate, memcached, memcached_spring
    ) dependsOn (
      core, ehcache, hibernate, memcached, memcached_spring
    )

  lazy val core = Project("simplecache-core", file("simplecache-core"))

  lazy val ehcache = Project("simplecache-ehcached", file("simplecache-ehcache")) dependsOn core
  lazy val hibernate = Project("simplecache-hibernate", file("simplecache-hibernate")) dependsOn core
  lazy val memcached = Project("simplecache-memcached", file("simplecache-memcached")) dependsOn core

  lazy val memcached_spring = Project("simplecache-memcached-spring", file("simplecache-memcached-spring")) dependsOn memcached
}