package com.igumnov.top3000words

import java.util.concurrent.Executors

import akka.actor.{ActorRef, Actor, Props}

import scala.concurrent.{Future, ExecutionContext}
import scala.util.{Success, Failure}

class EnglishDownloadActor(englishTranslationActor: ActorRef) extends Actor {
  println("EnglishDownloadActor")

  val getPageThreadExecutionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(15))

  def receive = {
    case (word: String) => {
      implicit val executor = getPageThreadExecutionContext
      Future {
        OxfordSite.getPage(word) match {
          case Success(pageText) => {
            println("englishTranslationActor ! EnglishPage("+word+", Some(pageText))")
            englishTranslationActor ! EnglishPage(word, Some(pageText))
          }
          case Failure(ex) => {
            englishTranslationActor ! EnglishPage(word, None)
            println(ex.getMessage)
          }
        }
      }
    }
  }

}