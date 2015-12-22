package com.igumnov.top3000words

case class Word (word: String, transcription: Option[String] = None, russianTranslation:Option[String] = None, englishTranslation: Option[String] = None)
case class RussianTranslation(word:String, translation: Option[String])
case class EnglishTranslation(word:String, translation: Option[String])
case class Transcription(word:String, transcription: Option[String])
case class Save(wordsCnt: Int)
case class EnglishPage(word: String, pageText: Option[String])
case class RussianPage(word: String, pageText: Option[String])