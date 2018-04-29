package com.twitteranalyzer.runner

import com.datastax.spark.connector.SomeColumns
import com.twitteranalyzer.database.CassandraClient
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import org.apache.spark.streaming.twitter.TwitterUtils
import com.datastax.spark.connector.streaming._
import com.twitteranalyzer.models.ClusteringLDA
import com.twitteranalyzer.utils.MessageUtils._
import twitter4j.Place

object TwitterRunner extends App {

  System.setProperty("twitter4j.oauth.consumerKey", "fPBSRfNmTwRmwSiKziGQnyRtY")
  System.setProperty("twitter4j.oauth.consumerSecret", "SWcVH2NjVfUm5jLOuB53S6AGKMdWvHx9gV4JDc042x3x7UFm28")
  System.setProperty("twitter4j.oauth.accessToken", "978286135356076032-wd5uDdKirJg6p0H54f2JeDJrqwlXW21")
  System.setProperty("twitter4j.oauth.accessTokenSecret", "WfmpFeVZYaGPPDm1Py7ttM1WV6e6FCdeFghSfDb36buAm")

  val sparkConf = new SparkConf().setAppName("runner").setMaster("local[2]")
  val streamingContext: StreamingContext = new StreamingContext(sparkConf, Seconds(5))
  val rootLogger = Logger.getRootLogger
  rootLogger.setLevel(Level.ERROR)
  val stream = TwitterUtils.createStream(streamingContext, None)
  val cassandraClient = new CassandraClient("localhost")
  cassandraClient.createSchema()
  val lda = new ClusteringLDA()

  case class Tweet(id: Long, createdAt: Long, text: String, lang: String, place: Place)
  stream.map(m => Tweet(m.getId, m.getCreatedAt.getTime / 1000, m.getText, m.getLang, m.getPlace))
    .filter(twit => twit.lang.equals("en"))
    .filter(twit => twit.place != null)
    .filter(twit => twit.place.getCountryCode.equals("US"))
    .map(twit => Tuple2(cleanUpMessage(twit.text.split(" ")), getHashtags(twit.text.split(" "))))
    .saveToCassandra("twits", "message", SomeColumns("message", "hashtag"))

  streamingContext.start()
  streamingContext.awaitTerminationOrTimeout(3600000)

  val rdd = streamingContext.cassandraTable("twits", "message").select("message")
  val rddArray = rdd.map(_.columnValues.toArray.mkString("\n"))

  val (ldaModel, vocabArray) = lda.runClustering(rddArray)

  val topicIndices = ldaModel.describeTopics(maxTermsPerTopic = 5)
  topicIndices.foreach {
    case (terms, termWeights) => {
      println("TOPIC:")
      terms.zip(termWeights).foreach { case (term, weight) =>
        println(s"${vocabArray(term.toInt)}\t$weight")
      }
      println()
    }
  }
}
