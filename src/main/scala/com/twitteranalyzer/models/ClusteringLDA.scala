package com.twitteranalyzer.models

import org.apache.spark.mllib.clustering.{LDA, LDAModel, OnlineLDAOptimizer}
import org.apache.spark.mllib.linalg.{Vector, Vectors}
import org.apache.spark.rdd.RDD

import scala.collection.mutable

class ClusteringLDA {

  val lda: LDA = new LDA().setOptimizer(new OnlineLDAOptimizer())
                          .setK(20).setMaxIterations(10)

  def runClustering(dataset: RDD[String]): (LDAModel, Array[String]) = {

    val tokenized: RDD[Seq[String]] =
      dataset.map(_.toLowerCase.split("\\s")).map(_.filter(_.length > 3)
        .filter(_.forall(java.lang.Character.isLetter)))
    val termCounts: Array[(String, Long)] =
      tokenized.flatMap(_.map(_ -> 1L)).reduceByKey(_ + _).collect().sortBy(-_._2)
    val vocabArray: Array[String] = termCounts.takeRight(termCounts.length).map(_._1)
    val vocab: Map[String, Int] = vocabArray.zipWithIndex.toMap

    val documents: RDD[(Long, Vector)] =
      tokenized.zipWithIndex.map { case (tokens, id) =>
        val counts = new mutable.HashMap[Int, Double]()
        tokens.foreach { term =>
        if (vocab.contains(term)) {
          val idx = vocab(term)
          counts(idx) = counts.getOrElse(idx, 0.0) + 1.0
        }
      }
      (id, Vectors.sparse(vocab.size, counts.toSeq))
    }

    (lda.run(documents), vocabArray)
  }
}
