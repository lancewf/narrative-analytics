package com.finfrock.narrative_analytics.model

import org.joda.time.DateTime

/**
  * Created by lancewf on 3/19/17.
  */
object AnalyticsTables {
  case class Event(userId:String, dateTime:DateTime, eventType:EventType)

  sealed trait EventType
  case object Click extends EventType
  case object Impression extends EventType

  def createEventType(eventTypeRaw:String):Option[EventType] ={
    eventTypeRaw.toLowerCase match{
      case "click" => Some(Click)
      case "impression" => Some(Impression)
      case _ => None
    }
  }

  def eventTypeToString(eventType:EventType):String ={
    eventType match{
      case Click => "click"
      case Impression => "impression"
    }
  }
}
