package com.finfrock.narrative_analytics.model

import com.datastax.driver.core.{Cluster, Session}
import com.finfrock.narrative_analytics.ConfigData

/**
  * Created by lancewf on 2/8/17.
  */
object QueryBuilder {

  private val sessionOption = buildDataSource
  private val localQuery = new AnalyticsQueryLocalStore()

  /**
    * Create a connection to the cassandra database.
    */
  def withAnalyticsQuery[A](op: AnalyticsQuery => A): A = {
    if (ConfigData.inProduction){
      sessionOption match{
        case Some(session) =>
          val cassandraQuery = new AnalyticsQueryCassandra(session)
          try {
            op(cassandraQuery)
          } finally {
            cassandraQuery.close() // close database connection.
          }
        case None =>
          throw new Exception("Error creating connection to Cassandra")
      }
    } else {
      op(localQuery)
    }
  }

  private def buildDataSource:Option[Session] ={
    try {
      val cluster = Cluster.builder().addContactPoint(ConfigData.cassandraContactServer).build()
      Some(cluster.connect(ConfigData.cassandraKeySpace))
    } catch{
      case _:Exception => None
    }
  }
}
