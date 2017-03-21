package com.finfrock.narrative_analytics

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.log4j.Logger

import scala.collection.JavaConversions._

object ConfigData {

  private val log = Logger.getLogger(getClass)
  private val config = ConfigFactory.load.getConfig("analytics")
  lazy val webServiceAddress:String = getWebServiceAddress(config)
  lazy val webServicePort:Int = getWebServicePort(config)
  lazy val servicePathName:String = getServicePathName(config)
  val inProduction:Boolean = getIsInProduction(config)
  val cassandraKeySpace:String = getCassandraKeySpace(config)
  val cassandraContactServer:String = getCassandraContactServer(config)
  val cassandraTableName:String = getCassandraTableName(config)

  private def getIsInProduction(config:Config):Boolean ={
    val inProduction = if(config.hasPath("in_production")){
      config.getBoolean("in_production")
    } else{
      true
    }

    if(inProduction){
      log.info("in production mode")
    } else{
      log.info("in test mode!!!!!")
    }

    inProduction
  }

  //"127.0.0.1"

  private def getCassandraTableName(config:Config):String ={
    if(config.hasPath("cassandra_table_name")){
      config.getString("cassandra_table_name")
    } else{
      "event"
    }
  }

  private def getCassandraContactServer(config:Config):String ={
    if(config.hasPath("cassandra_contact_server")){
      config.getString("cassandra_contact_server")
    } else{
      "127.0.0.1"
    }
  }

  private def getCassandraKeySpace(config:Config):String ={
    if(config.hasPath("cassandra_key_space")){
      config.getString("cassandra_key_space")
    } else{
      "test"
    }
  }

  private def getWebServiceAddress(config:Config):String ={
    if(config.hasPath("web_service_address")){
      config.getString("web_service_address")
    } else{
      "0.0.0.0"
    }
  }

  private def getServicePathName(config:Config):String ={
    if(config.hasPath("service_path_name")){
      config.getString("service_path_name")
    } else{
      "analytics"
    }
  }

  private def getWebServicePort(config:Config):Int ={
    if(config.hasPath("web_service_port")){
      config.getInt("web_service_port")
    } else{
      8080
    }
  }
}