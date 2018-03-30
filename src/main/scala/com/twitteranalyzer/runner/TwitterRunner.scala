package com.twitteranalyzer.runner

import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkConf
import org.apache.spark.streaming._
import org.apache.spark.streaming.twitter.TwitterUtils

object TwitterRunner extends App {

  System.setProperty("twitter4j.oauth.consumerKey", "fPBSRfNmTwRmwSiKziGQnyRtY")
  System.setProperty("twitter4j.oauth.consumerSecret", "SWcVH2NjVfUm5jLOuB53S6AGKMdWvHx9gV4JDc042x3x7UFm28")
  System.setProperty("twitter4j.oauth.accessToken", "978286135356076032-wd5uDdKirJg6p0H54f2JeDJrqwlXW21")
  System.setProperty("twitter4j.oauth.accessTokenSecret", "WfmpFeVZYaGPPDm1Py7ttM1WV6e6FCdeFghSfDb36buAm")

  val sparkConf = new SparkConf().setAppName("runner").setMaster("local[2]")
  val streamingContext: StreamingContext = new StreamingContext(sparkConf, Milliseconds(15000))
  val rootLogger = Logger.getRootLogger
  rootLogger.setLevel(Level.ERROR)
  val stream = TwitterUtils.createStream(streamingContext, None)

  case class Tweet(createdAt: Long, text: String, lang: String)
  val twits = stream.window(Seconds(15)).map(m =>
    Tweet(m.getCreatedAt.getTime / 1000, m.toString, m.getLang)
  )

  twits.foreachRDD(rdd => rdd.collect().filter(twit => twit.lang.equals("en")).foreach(println))

  streamingContext.start()
  streamingContext.awaitTermination()
}
