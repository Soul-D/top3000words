package com.igumnov.top3000words

import java.util.concurrent.Executors

import akka.actor.ActorSystem
import akka.actor.Props
import scala.concurrent.{Future, ExecutionContext}
import scala.util.Success
import scala.util.Failure

object Top3000WordsApp extends App {


  val system = ActorSystem("Top3000Words")
  val dictionatyActor = system.actorOf(Props[DictionaryActor], "dictionatyActor")
  val englishTranslationActor = system.actorOf(Props(classOf[EnglishTranslationActor], dictionatyActor), "englishTranslationActor")
  val russianTranslationActor = system.actorOf(Props(classOf[RussianTranslationActor], dictionatyActor), "russianTranslationActor")
  val englishDownloadActor = system.actorOf(Props(classOf[EnglishDownloadActor], englishTranslationActor), "englishDownloadActor")
  val russianDownloadActor = system.actorOf(Props(classOf[RussianDownloadActor], russianTranslationActor), "russianDownloadActor")


  val wordsCnt = OxfordSite.getTableOfContent.flatMap(letterGroup => {
    getWords(letterGroup, 1)
  }).size

  dictionatyActor ! Save(wordsCnt)

  def getWords(letterGroup: String, pageNum: Int): List[String] = {

    OxfordSite.getWordsFromPage(letterGroup, pageNum) match {
      case Success(Some(words)) => {
        words.foreach((word: String) => {
          println("englishDownloadActor ! " + word)
          englishDownloadActor ! word
          println("russianDownloadActor ! " + word)
          russianDownloadActor ! word
        })
        words ++ getWords(letterGroup, pageNum+1)
      }
      case Success(None) => List.empty
      case Failure(ex) => {
        println(ex.getMessage)
        List.empty
      }
    }
  }


}
