name := "twitter-analyzer"

version := "0.1"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-streaming" % "2.0.1",
  "org.apache.bahir" %% "spark-streaming-twitter" % "2.0.1",
  "com.typesafe" % "config" % "1.2.1",
  "com.datastax.spark" %% "spark-cassandra-connector" % "2.0.5",
  "org.apache.spark" % "spark-sql_2.11" % "2.0.0",
  "org.apache-extras.cassandra-jdbc" % "cassandra-jdbc" % "1.2.5",
  "org.apache.spark" % "spark-mllib_2.11" % "2.0.0"
)