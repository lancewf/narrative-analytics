package com.finfrock.narrative_analytics

import akka.actor.Props

/**
 * This class is used in the Akka Microkernal.
 */
object Bootable extends App {

  ThreadsContainer.actorSystem.actorOf(Props[WebServer], "WebServer")
}