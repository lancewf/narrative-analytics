package com.finfrock.narrative_analytics.model

import akka.agent.Agent
import com.finfrock.narrative_analytics.ThreadsContainer
import com.finfrock.narrative_analytics.model.AnalyticsTables.Event
import org.joda.time.DateTime

import scala.concurrent.{ExecutionContext, Future}

class EventCache {
  private implicit val executionContext:ExecutionContext = ThreadsContainer.cpuBoundThreadPool
  private val eventCollectionAgent = Agent(List[Event]())

  def getEventsByDate(startDate:DateTime, endDate:DateTime):Future[List[Event]] ={
    eventCollectionAgent.future().map(
      _.filter(event => event.dateTime.isAfter(startDate) && event.dateTime.isBefore(endDate)))
  }

  def addEvent(event:Event) {
    eventCollectionAgent.send(eventCollection => event :: eventCollection)
  }
}
