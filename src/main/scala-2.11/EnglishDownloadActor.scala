package com.igumnov.top3000words

import java.util.concurrent.Executors

import akka.actor.{ActorRef, Actor, Props}

import scala.concurrent.ExecutionContext
import scala.util.{Success, Failure}

class EnglishDownloadActor(englishTranslationActor: ActorRef) extends Actor {
  println("EnglishDownloadActor")

  val getPageThreadExecutionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(50))

  def receive = {
    case (word: String) => {
      implicit val executor = getPageThreadExecutionContext
      val future = OxfordSite.getPage(word)
      future.onSuccess {
        case pageText: String => {
          println("englishTranslationActor ! EnglishPage("+word+", Some(pageText))")
          englishTranslationActor ! EnglishPage(word, Some(pageText))
        }
      }
      future.onFailure {
        case ex => {
          englishTranslationActor ! EnglishPage(word, None)
          println(ex.getMessage)
        }
      }
    }
  }

}