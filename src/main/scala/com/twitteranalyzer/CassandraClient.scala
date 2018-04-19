package com.twitteranalyzer

import com.datastax.driver.core.{Cluster, Session}

class CassandraClient(node: String) {

  private val cluster = Cluster.builder().addContactPoint(node).build()
  val session: Session = cluster.connect()

  def createSchema(): Unit = {
    session.execute("CREATE KEYSPACE IF NOT EXISTS twits " +
      "WITH REPLICATION = {'class':'SimpleStrategy', 'replication_factor':1};")
    session.execute("USE twits;")
    session.execute("CREATE TABLE IF NOT EXISTS twits.message (message text PRIMARY KEY, hashtag text);")
  }

  def close() {
    session.close()
    cluster.close()
  }
}
