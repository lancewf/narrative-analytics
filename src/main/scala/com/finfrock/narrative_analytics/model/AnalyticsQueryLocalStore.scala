package com.finfrock.narrative_analytics.model

import AnalyticsTables.Event
import org.joda.time.{DateTime, DateTimeZone}

import scala.concurrent.Future

/**
  * Created by lancewf on 2/8/17.
  */
class AnalyticsQueryLocalStore extends AnalyticsQuery {

  private val eventCache = new EventCache()

  def getEventsForHour(date:DateTime):Future[List[Event]] ={
    val (startDate, endDate) = getStartAndEndDate(date)
    eventCache.getEventsByDate(startDate, endDate)
  }

  def addEvent(event:Event) {
    eventCache.addEvent(event)
  }

  def close(){}

  private def getStartAndEndDate(date:DateTime):(DateTime, DateTime) ={
    val startDate = new DateTime(date.getYear, date.getMonthOfYear, date.getDayOfMonth,
      date.getHourOfDay, 0, 0, 0, DateTimeZone.UTC)

    val endDate = startDate.plusHours(1)

    (startDate.minusMillis(1), endDate)
  }
}
