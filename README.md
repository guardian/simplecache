Simplecache
===========

What is it?
-----------

`Simplecache` is intended to be a cache interface library with a
simple abstraction. At present, `simplecache` supports EhCache,
Memcached and `java.util.concurrent.ConcurrentMap` backends.


How do I use it?
----------------

In `sbt`, add the Guardian repository to your `build.sbt`:

   resolvers += "Guardian Github Releases" at "http://guardian.github.com/maven/repo-releases"

Find the latest version by inspecting the available
`simplecache-core` artifacts in the repository at:

   http://guardian.github.com/maven/repo-releases/com/gu/simplecache-core

Then include at least the `simplecache-core` artifact in your `build.sbt`:

   libraryDependencies += "com.gu" % "simplecache-core" % "2.1"

Depending on your application requirements you may want to also
include some backend projects. At present, the available artifacts
include:

* `simplecache-core`: This is the base artifact, including the
  interfaces and a `java.util.concurrent.ConcurrentMap` implementation
  using soft references.

* `simplecache-ehcache`: This includes an adaptor and dependencies
  for using an EhCache backed simplecache.

* `simplecache-memcached`: This includes an adaptor, statistics
  reporting and dependencies for using a Memcached backed simplecache.

* `simplecache-hibernate`: Hibernate `Region`s and `AccessStrategy`
  implementations facilitating the use of simplecaches for Hibernate
  caches.

* `simplecache-memcached-spring`: Spring `FactoryBean`s for dependency
  injecting `simplecache-memcached` code into a Spring application
  context.

If, like us, you have a Spring/Hibernate application with an Ehcache second
level Hibernate cache backing to a shared Memcached, you might find it
easier to include all of the above using the `simplecache-all` convenience
package.

`SimpleCache` in the `simplecache-core` project is the interface of
interest. Put your hands on something that implements that. Some ways of
doing that are:

* New up a `SoftReferenceSimpleCache`.
* New up an `EhcacheSimpleCacheAdaptor` from and `Ehcache` object.
* Create a `SpyMemcachedClientAdaptor` using objects from the
  `spy-memcached` library and an `MD5KeyTranslator` to new up a
  `MemcachedSimpleCacheAdaptor`.
* Use the `TwoLevelSimpleCacheAggregator` to build a cache chain
  from existing `SimpleCache` objects.



How do I build it?
------------------

Simplecache is a scala project built using [simple-build-tool][sbt] 0.11.
Build using:

	./sbt package

To use in intellij, setup and use the idea plugin by creating the
following definition in `~/.sbt/plugins/build.sbt`:

    resolvers += "sbt-idea-repo" at "http://mpeltonen.github.com/maven/"

    libraryDependencies <+= (sbtVersion) {
      case "0.10.0" => "com.github.mpeltonen" % "sbt-idea_2.8.1" % "0.10.0" from "http://mpeltonen.github.com/maven/com/github/mpeltonen/sbt-idea_2.8.1/0.10.0/sbt-idea_2.8.1-0.10.0.jar"
      case "0.10.1" => "com.github.mpeltonen" % "sbt-idea_2.8.1" % "0.10.1-SNAPSHOT" from "http://mpeltonen.github.com/maven/com/github/mpeltonen/sbt-idea_2.8.1/0.10.1-SNAPSHOT/sbt-idea_2.8.1-0.10.1-SNAPSHOT.jar"
      case "0.11.0" => "com.github.mpeltonen" % "sbt-idea_2.9.1" % "0.11.0" from "http://mpeltonen.github.com/maven/com/github/mpeltonen/sbt-idea_2.9.1_0.11.0/0.11.0/sbt-idea-0.11.0.jar"
      case sbtv => error("Unsupported SBT version: " + sbtv)
    }


Then run the following in interactive `sbt`:

    gen-idea

The `sbt` console is very useful. FYou can do e.g. the following:

    > console
    ...
    Welcome to Scala version 2.9.1.final (Java HotSpot(TM) 64-Bit Server VM, Java 1.6.0_16).
    Type in expressions to have them evaluated.
    Type :help for more information.

    scala> ...

Further documentation notes and useful items can be found in `dev`.


How do I release it?
--------------------

* Clone the `guardian.github.com` repository to your home directory.

* Update the version numbers in `simplecache` to the new release version number.

* Invoke `sbt` and run the `publish` goal. This will create the necessary
  structure in the cloned `guardian.github.com` project.

* Run `update-directory-index.sh` from the `guardian.github.com` clone to
  refresh the HTML index pages.

* Commit and push `guardian.github.com`.

* Update the version numbers in `simplecache` to the new development version number.

* Commit and push `simplecache`.


[sbt]: http://books.google.com/p/simple-build-tool/
[scalariform]: http://github.com/olim7t/sbt-scalariform
[markdown]: http://daringfireball.net/projects/markdown
