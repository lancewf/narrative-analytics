package com.finfrock.narrative_analytics

import java.util.concurrent.Executors

import com.finfrock.narrative_analytics.model.{AnalyticsTables, QueryBuilder}
import com.finfrock.narrative_analytics.model.AnalyticsTables.Event
import org.joda.time.DateTime
import org.junit._
import org.junit.Assert._

import scala.concurrent.ExecutionContext

/**
  * Created by lancewf on 3/19/17.
  */
@Test
class CassandraTest {

  @Test
  def testOk() = assertTrue(true)

  @Test
  def testAdd() {
    QueryBuilder.withAnalyticsQuery(analyticsQuery =>{

      val event = Event("lancewf@gmail.com", DateTime.now, AnalyticsTables.Click)
      analyticsQuery.addEvent(event)
    })
  }

  @Test
  def testGetValues() {
    implicit val cpuBoundThreadPool = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(4))
    QueryBuilder.withAnalyticsQuery(analyticsQuery => {
      val endDate = DateTime.now()
      val eventsFuture = analyticsQuery.getEventsForHour(endDate)

      eventsFuture.map(events =>{
        println(events.mkString("\n"))
      })

    })
  }

}
