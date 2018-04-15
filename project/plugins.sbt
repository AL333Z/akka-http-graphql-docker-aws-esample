resolvers += Resolver.url("bintray-sbilinski", url("http://dl.bintray.com/sbilinski/maven"))(Resolver.ivyStylePatterns)

addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1")
addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.9.3")
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.2")
addSbtPlugin("com.mintbeans" % "sbt-ecr" % "0.8.0")


libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-nop" % "1.7.25" // Needed by sbt-git
)
