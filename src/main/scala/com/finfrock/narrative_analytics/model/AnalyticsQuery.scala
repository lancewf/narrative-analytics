package com.finfrock.narrative_analytics.model

import AnalyticsTables.Event
import org.joda.time.DateTime

import scala.concurrent.Future

/**
  * Created by lancewf on 3/19/17.
  */
trait AnalyticsQuery{

  def getEventsForHour(date:DateTime):Future[List[Event]]

  def addEvent(event:Event)

  def close()
}
