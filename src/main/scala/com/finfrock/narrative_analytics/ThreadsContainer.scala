package com.finfrock.narrative_analytics

import java.util.concurrent.Executors

import akka.actor.ActorSystem

import scala.concurrent.{ExecutionContext, ExecutionContextExecutorService}

object ThreadsContainer {

  val actorSystem:ActorSystem = createActorSystem()
  val ioBoundThreadPool:ExecutionContextExecutorService = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(500))
  val cpuBoundThreadPool:ExecutionContextExecutorService  = createCpuBoundThreadPool()


  private def createActorSystem(): ActorSystem = {
      ActorSystem("Analytics")
  }

  private def createCpuBoundThreadPool(): ExecutionContextExecutorService = {
    val numberOfCores = Runtime.getRuntime.availableProcessors()
    ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(numberOfCores * 2))
  }
}
