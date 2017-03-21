package com.finfrock.narrative_analytics.model

import com.datastax.driver.core.{ResultSet, Session}
import AnalyticsTables.Event
import CassandraWrapper._
import com.finfrock.narrative_analytics.{ConfigData, ThreadsContainer}
import org.joda.time.{DateTime, DateTimeZone}

import scala.collection.JavaConversions._
import scala.concurrent.Future

/**
  * Created by lancewf on 2/8/17.
  */
class AnalyticsQueryCassandra(session: Session) extends AnalyticsQuery {

  private implicit val executorService = ThreadsContainer.ioBoundThreadPool
  private val tableName = ConfigData.cassandraTableName
  private val addEventPreparedStatement = session.prepare(
    s"insert into $tableName (user_id, date_time, hourlytime, event_type) values (?, ?, ?, ?)")

  private val getEventPreparedStatement = session.prepare(
    s"Select * FROM $tableName WHERE hourlytime = ?")

  def close(){
    //do not close.
  }

  def getEventsForHour(date:DateTime):Future[List[Event]] ={
    val roundDateTime = roundDateOffToHour(date)
    val bound = getEventPreparedStatement.bind(roundDateTime.toDate)

    val resultsFuture:Future[ResultSet] = session.executeAsync(bound)

    resultsFuture.map(results => {
      (for {row <- results
            userId = row.getString("user_id")
            dateRaw = row.getTimestamp("date_time")
            dateTime = new DateTime(dateRaw, DateTimeZone.UTC)
            rawEventType = row.getString("event_type").toLowerCase
            eventType <- AnalyticsTables.createEventType(rawEventType)} yield {
        Event(userId, dateTime, eventType)
      }).toList
    })
  }

  private def roundDateOffToHour(date:DateTime):DateTime ={
    new DateTime(date.getYear, date.getMonthOfYear, date.getDayOfMonth,
      date.getHourOfDay, 0, 0, 0, DateTimeZone.UTC)
  }

  def addEvent(event:Event) {
    val eventTypeString = AnalyticsTables.eventTypeToString(event.eventType)

    val fullDate = event.dateTime.toDate
    val roundedDate = roundDateOffToHour(event.dateTime).toDate

    val bound = addEventPreparedStatement.bind(event.userId,
      fullDate, roundedDate, eventTypeString)

    session.executeAsync(bound)
  }
}
