import scalariform.formatter.preferences._
import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys

name := "athena-sample"

organization := "org.grimrose"

version := "0.1.0"

scalaVersion := "2.12.0"

resolvers ++= Seq(
  "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"
)

unmanagedBase := baseDirectory.value / "libs"

libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc" % "2.5.0",
  "com.h2database" % "h2" % "1.4.193",
  "com.zaxxer" % "HikariCP" % "2.4.+",

  "com.amazonaws" % "aws-java-sdk-core" % "1.11.66",

  "ch.qos.logback"  %  "logback-classic"   % "1.1.7",

  "org.scalatest" %% "scalatest" % "3.0.0" % "test",
  "org.scalacheck" %% "scalacheck" % "1.13.4" % "test"
)

scalacOptions ++= Seq(
  "-target:jvm-1.8",
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:experimental.macros",
  "-unchecked",
  //"-Ywarn-unused-import",
  "-Ywarn-nullary-unit",
  "-Xfatal-warnings",
  "-Xlint",
  //"-Yinline-warnings",
  "-Ywarn-dead-code",
  "-Xfuture")

initialCommands :=
  """ import scalikejdbc._
  """

SbtScalariform.scalariformSettings

ScalariformKeys.preferences := ScalariformKeys.preferences.value
  .setPreference(AlignSingleLineCaseStatements, true)
  .setPreference(DoubleIndentClassDeclaration, true)
  .setPreference(RewriteArrowSymbols, true)
