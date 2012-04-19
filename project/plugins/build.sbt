resolvers ++= Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases",
  "spray repo" at "http://repo.spray.cc"
)

libraryDependencies <+= sbtVersion(v => "com.github.siasia" %% "xsbt-web-plugin" % (v+"-0.2.11"))

addSbtPlugin("com.typesafe.sbtscalariform" % "sbtscalariform" % "0.3.0")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.7.4")

addSbtPlugin("com.eed3si9n" % "sbt-appengine" % "0.4.0")

