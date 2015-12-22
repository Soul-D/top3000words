package com.igumnov.top3000words

import akka.actor.Actor

class DictionaryActor extends Actor {
  println("DictionaryActor")

  def receive = active(Map.empty, 0 , 0)

  def active(words: Map[String, Word], callsNumber: Int, size: Int): Receive = {
    case Transcription(wordName, transcription) => {
      val newElement = words.get(wordName) match {
        case Some(word) => word.copy(transcription = transcription)
        case None => Word(wordName, transcription = transcription)
      }
      context become active(words + (wordName -> newElement), callsNumber + 1, size)
      println(newElement)
      save(words + (wordName -> newElement), callsNumber + 1, size)
    }
    case RussianTranslation(wordName, translation) => {
      val newElement = words.get(wordName) match {
        case Some(word) => word.copy(russianTranslation = translation)
        case None => Word(wordName, russianTranslation = translation)
      }
      context become active(words + (wordName -> newElement), callsNumber + 1, size)
      println(newElement)
      save(words + (wordName -> newElement), callsNumber + 1, size)
    }
    case EnglishTranslation(wordName, translation) => {
      val newElement = words.get(wordName) match {
        case Some(word) => word.copy(englishTranslation = translation)
        case None => Word(wordName, englishTranslation =translation)
      }
      context become active(words + (wordName -> newElement), callsNumber + 1, size)
      println(newElement)
      save(words + (wordName -> newElement), callsNumber + 1, size)
    }
    case Save(size) => {
      context become active(words, callsNumber, size)
    }
  }


  private[this] def save( words: Map[String, Word],  callsNumber:Int, size:Int) = {
    if (size * 3 == callsNumber) {
      val fileText = words.map { case (_, someWord) => {
        val transcription = someWord.transcription.getOrElse(" ")
        val russianTranslation = someWord.russianTranslation.getOrElse(" ")
        val englishTranslation = someWord.englishTranslation.getOrElse(" ")
        List(someWord.word, transcription, russianTranslation, englishTranslation).mkString("|")
      }
      }.mkString("\n")
      scala.tools.nsc.io.File("dictionary.txt").writeAll(fileText)
      println("dictionary.txt saved")
      context.system.terminate()
    } else {
      println("Cant save " + (size*3) + "!=" + callsNumber)
      //self ! Save(size)
    }
  }


}