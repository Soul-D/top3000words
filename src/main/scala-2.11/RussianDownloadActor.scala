package com.igumnov.top3000words

import java.util.concurrent.Executors

import akka.actor.{ActorRef, Actor, Props}

import scala.concurrent.{Future, ExecutionContext}
import scala.util.{Success, Failure}

class RussianDownloadActor(russianTranslationActor: ActorRef) extends Actor {
  println("RussianDownloadActor")

  val getPageThreadExecutionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(15))

  def receive = {
    case (word: String) => {
      implicit val executor = getPageThreadExecutionContext
      Future {
        LingvoSite.getPage(word) match {
          case Success(pageText) => {
            println("russianTranslationActor ! RussianPage(" + word + ", Some(pageText))")
            russianTranslationActor ! RussianPage(word, Some(pageText))

          }
          case Failure(ex) => {
            russianTranslationActor ! RussianPage(word, None)
            println(ex.getMessage)
          }
        }
      }
    }
  }

}