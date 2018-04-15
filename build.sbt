// *****************************************************************************
// Projects
// *****************************************************************************

lazy val `sangria-akka-http-example` =
  project
    .in(file("."))
    .enablePlugins(GitVersioning, DockerPlugin, JavaAppPackaging)
    .settings(settings)
    .settings(
      libraryDependencies ++= Seq(
        "org.sangria-graphql" %% "sangria" % "1.4.0",
        "org.sangria-graphql" %% "sangria-circe" % "1.2.1",

        "com.typesafe.akka" %% "akka-http" % "10.1.0",
        "de.heikoseeberger" %% "akka-http-circe" % "1.20.0",

        "io.circe" %% "circe-core" % "0.9.2",
        "io.circe" %% "circe-parser" % "0.9.2",
        "io.circe" %% "circe-optics" % "0.9.2",

        "org.mongodb.scala" %% "mongo-scala-driver" % "2.2.1",

        "org.scalatest" %% "scalatest" % "3.0.5" % Test
      )
    )

// *****************************************************************************
// Push to ECR
// *****************************************************************************
import com.amazonaws.regions.{Region, Regions}

enablePlugins(EcrPlugin)

region in Ecr := Region.getRegion(Regions.EU_WEST_1)
repositoryName in Ecr := (packageName in Docker).value
localDockerImage in Ecr := (packageName in Docker).value + ":" + (version in Docker).value

push in Ecr := ((push in Ecr) dependsOn (publishLocal in Docker, login in Ecr)).value

// *****************************************************************************
// Settings
// *****************************************************************************

lazy val settings =
  commonSettings ++
    gitSettings ++
    dockerSettings

lazy val commonSettings =
  Seq(
    organization := "com.al333z",
    organizationName := "Alessandro Zoffoli",
    startYear := Some(2018),
    scalaVersion := "2.12.4",
    licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0")),
    scalacOptions ++= Seq(
      "-unchecked",
      "-deprecation",
      "-language:_",
      "-target:jvm-1.8",
      "-encoding",
      "UTF-8"
    ),
    mainClass.in(Compile) := Some("Server"),
    unmanagedSourceDirectories.in(Compile) := Seq(scalaSource.in(Compile).value),
    unmanagedSourceDirectories.in(Test) := Seq(scalaSource.in(Test).value),
    publishArtifact.in(Compile, packageDoc) := false,
    publishArtifact.in(Compile, packageSrc) := false
  )

lazy val gitSettings =
  Seq(
    git.useGitDescribe := true
  )

lazy val dockerSettings =
  Seq(
    version.in(Docker) := "latest",
    dockerBaseImage := "openjdk:8u151-slim",
    dockerExposedPorts := Vector(9000),
  )
