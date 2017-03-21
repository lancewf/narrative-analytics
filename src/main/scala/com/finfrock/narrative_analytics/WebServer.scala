package com.finfrock.narrative_analytics

import akka.actor.Actor
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.finfrock.narrative_analytics.model.{AnalyticsTables, QueryBuilder}
import com.finfrock.narrative_analytics.model.AnalyticsTables.{Click, Event, Impression}
import org.apache.log4j.Logger
import org.joda.time.{DateTime, DateTimeZone}

import scala.concurrent.Future

object WebServer{
  object START
  object STOP
}

class WebServer extends Actor  {
  private val log = Logger.getLogger(getClass)
  private implicit val system = ThreadsContainer.actorSystem
  private implicit val materializer = ActorMaterializer()
  private implicit val executionContext = ThreadsContainer.actorSystem.dispatcher

  private var bindingFutureOption:Option[Future[ServerBinding]] = None
  private val servicePathName = ConfigData.servicePathName

  self ! WebServer.START

  def receive:Receive = {
    case WebServer.START ⇒ start()
    case WebServer.STOP ⇒ stop()
  }

  private def start() {
    bindingFutureOption = Some(Http().bindAndHandle(buildRoute(),
      ConfigData.webServiceAddress, ConfigData.webServicePort))

    log.info("Web Server online at " + ConfigData.webServiceAddress + ":" + ConfigData.webServicePort)
  }

  private def stop(){
    bindingFutureOption match{
      case Some(bindingFuture) =>
        bindingFuture.flatMap(_.unbind())
      case None => //do nothing it was never started.
    }
  }

  private def buildRoute():Route ={
    path(servicePathName) {
      post {
        parameters('timestamp, 'user, 'event) { (timestampMillsUtc, userId, eventTypeRaw) =>

          createEvent(userId, timestampMillsUtc, eventTypeRaw) match{
            case Some(event) =>
              QueryBuilder.withAnalyticsQuery(analyticsQuery => {
                analyticsQuery.addEvent(event)
              })
              complete(StatusCodes.NoContent)
            case None =>
              complete(StatusCodes.NotFound, "Data is in incorrect format")
          }
        }
      } ~
      get {
        parameters('timestamp) { (timestampMillsUtc) =>

          val requestedDateTime = new DateTime(timestampMillsUtc.toLong, DateTimeZone.UTC)

          val textFuture = QueryBuilder.withAnalyticsQuery(analyticsQuery => {
            analyticsQuery.getEventsForHour(requestedDateTime).map(events => {
              val numberOfUniqueUserNames = events.map(_.userId).distinct.size
              val numberOfClicks = events.count(_.eventType == Click)
              val numberOfImpressions = events.count(_.eventType == Impression)

              s"unique_users,$numberOfUniqueUserNames\n" +
                s"clicks,$numberOfClicks\n" +
                s"impressions,$numberOfImpressions"
            })
          })

          complete {
            textFuture.map(text => HttpEntity(ContentTypes.`text/plain(UTF-8)`, text))
          }
        }
      }
    } ~
    path(servicePathName / "healthtest") {
      get {
        complete(HttpEntity(ContentTypes.`application/json`, "Everything is working"))
      }
    }
  }

  private def createEvent(userId:String, timestampMillsUtcRaw:String, eventTypeRaw:String):Option[Event] ={
    for{eventType <- AnalyticsTables.createEventType(eventTypeRaw)
        dateTime <- parseDateTime(timestampMillsUtcRaw)} yield {
      Event(userId, dateTime, eventType)
    }
  }

  private def parseDateTime(timestampMillsUtcRaw:String):Option[DateTime] ={
    try {
      Some(new DateTime(timestampMillsUtcRaw.toLong, DateTimeZone.UTC))
    }
    catch {
      case _:Exception =>
        None
    }
  }
}
