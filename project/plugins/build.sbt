resolvers ++= Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases",
  "Siasia Repository" at "http://siasia.github.com/maven2"
)

addSbtPlugin("com.github.siasia" % "xsbt-web-plugin" % "0.1.2")

addSbtPlugin("com.typesafe.sbtscalariform" % "sbt-scalariform" % "0.1.4")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.7.1")

addSbtPlugin("com.eed3si9n" % "sbt-appengine" % "0.3.0")
