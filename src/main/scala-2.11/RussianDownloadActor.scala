package com.igumnov.top3000words

import java.util.concurrent.Executors

import akka.actor.{ActorRef, Actor, Props}

import scala.concurrent.ExecutionContext
import scala.util.{Success, Failure}

class RussianDownloadActor(russianTranslationActor: ActorRef) extends Actor {
  println("RussianDownloadActor")

  val getPageThreadExecutionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(10))

  def receive = {
    case (word: String) => {
      implicit val executor = getPageThreadExecutionContext
      val future = LingvoSite.getPage(word)
      future.onSuccess {
        case pageText: String => {
          println("russianTranslationActor ! RussianPage("+word+", Some(pageText))")
          russianTranslationActor ! RussianPage(word, Some(pageText))
        }
      }
      future.onFailure {
        case ex => {
          russianTranslationActor ! RussianPage(word, None)
          println(ex.getMessage)
        }
      }
    }
  }

}