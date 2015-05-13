name := "spark-protobuf"

version := "1.0"

scalaVersion := "2.10.5"

resolvers ++= Seq(
  "Twttr" at "http://maven.twttr.com/"
)

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.3.1" % "provided",
  "com.twitter.elephantbird" % "elephant-bird" % "4.6",
  "com.twitter.elephantbird" % "elephant-bird-core" % "4.6",
  "com.google.protobuf" % "protobuf-java" % "2.5.0"
)

assemblyMergeStrategy in assembly := {
  //This sauce below is to prevent all kinds of issues on deployment of the assembly, including SecurityException
  case PathList("META-INF", xs@_*) => MergeStrategy.discard
  case x => MergeStrategy.first
}