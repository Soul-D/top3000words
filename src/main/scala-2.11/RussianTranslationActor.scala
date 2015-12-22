package com.igumnov.top3000words

import akka.actor.{ActorRef, Actor, Props}

import scala.util.{Failure, Success}

class RussianTranslationActor  (dictionaryActor: ActorRef) extends Actor {
  println("RussianTranslationActor")


  def receive = {
    case RussianPage(word, Some(russianPage)) => {
      LingvoSite.parseTranslation(russianPage) match {
        case Success(translation) => {
          dictionaryActor ! RussianTranslation(word,Some(translation))
        }
        case Failure(ex) => {
          dictionaryActor ! RussianTranslation(word,None)
          println(ex.getMessage)
        }
      }
    }
    case RussianPage(word, None) => {
      dictionaryActor ! RussianTranslation(word,None)
    }

  }



}
