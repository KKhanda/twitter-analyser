package com.twitteranalyzer.utils

object MessageUtils {

  val stopWords = List("a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are", "aren't", "as", "at",
    "be", "because", "been", "before", "being", "below", "between", "both", "but", "by",
    "can", "can't", "come", "could", "couldn't",
    "d", "did", "didn't", "do", "does", "doesn't", "doing", "don't", "down", "during",
    "each",
    "few", "finally", "for", "from", "further",
    "had", "hadn't", "has", "hasn't", "have", "haven't", "having", "he", "her", "here", "hers", "herself", "him", "himself", "his", "how",
    "i", "if", "in", "into", "is", "isn't", "it", "its", "itself",
    "just",
    "ll",
    "m", "me", "might", "more", "most", "must", "my", "myself",
    "no", "nor", "not", "now",
    "o", "of", "off", "on", "once", "only", "or", "other", "our", "ours", "ourselves", "out", "over", "own",
    "r", "re",
    "s", "said", "same", "she", "should", "shouldn't", "so", "some", "such",
    "t", "than", "that", "that's", "the", "their", "theirs", "them", "themselves", "then", "there", "these", "they", "this", "those", "through", "to", "too",
    "under", "until", "up",
    "very",
    "was", "wasn't", "we", "were", "weren't", "what", "when", "where", "which", "while", "who", "whom", "why", "will", "with", "wont", "would",
    "y", "you", "your", "yours", "yourself", "yourselves")

  val urlPattern = "((https?|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)"
  val aliasPattern = "((@)*)"
  val hashtagPattern = "\\B(\\#[a-zA-Z]+\\b)(?!;)"

  def cleanUpMessage(message: Array[String]): String = {
    message
      .map(word => word.toLowerCase())
      .filter(word => !stopWords.contains(word))
      .map(word => word.replaceAll(urlPattern, ""))
      .map(word => word.replaceAll(aliasPattern, ""))
      .map(word => word.replaceAll(hashtagPattern, ""))
      .mkString(" ")
  }

  def getHashtags(message: Array[String]): String = {
    message.filter(word => word matches hashtagPattern).mkString(" ")
  }

}
