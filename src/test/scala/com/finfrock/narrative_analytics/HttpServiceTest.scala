package com.finfrock.narrative_analytics

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest}
import akka.stream.ActorMaterializer
import org.joda.time.{DateTime, DateTimeZone}
import org.junit._
import org.junit.Assert._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.util.Random

/**
  * Created by lancewf on 3/20/17.
  */
@Test
class HttpServiceTest {

//  @Test
//  def testAddEvent() {
//    implicit val actorSystem = ActorSystem("TestSystem")
//    implicit val materializer = ActorMaterializer()
//    implicit  val executionContext = actorSystem.dispatcher
//
//    val users = Array("bob", "tim", "fran", "al")
//    val random = new Random()
//    val dateTime = DateTime.now(DateTimeZone.UTC)
//
//    val requests = for{i <- 0 until 30
//        dateInMillis = dateTime.minusMinutes(i).getMillis} yield {
//      val eventType = if(random.nextBoolean()){
//        "click"
//      } else{
//        "impression"
//      }
//      val user = users(random.nextInt(users.length))
//      Http().singleRequest(HttpRequest(method = HttpMethods.POST, uri = s"http://localhost:8080/analytics?timestamp=$dateInMillis&user=$user&event=$eventType"))
//    }
//
//    Await.result(Future.sequence(requests), Duration.Inf)
//  }
}
