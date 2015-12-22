package com.igumnov.top3000words

import akka.actor.{ActorRef, Actor, Props}

import scala.util.{Success, Failure}

class EnglishTranslationActor (dictionaryActor: ActorRef) extends Actor {
  println("EnglishTranslationActor")


  def receive = {
    case EnglishPage(word, Some(englishPage)) => {
      OxfordSite.parseTranslation(englishPage) match {
        case Success((transcription, translation)) => {
          dictionaryActor ! EnglishTranslation(word,Some(translation))
          dictionaryActor ! Transcription(word,Some(transcription))
        }
        case Failure(ex) => {
          dictionaryActor ! EnglishTranslation(word,None)
          dictionaryActor ! Transcription(word,None)
          println(ex.getMessage)
        }
      }
    }
    case EnglishPage(word, None) => {
      dictionaryActor ! EnglishTranslation(word,None)
      dictionaryActor ! Transcription(word,None)
    }

  }

}