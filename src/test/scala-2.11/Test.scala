package com.igumnov.top3000words.test
import com.igumnov.top3000words._
import org.scalatest._
import akka.testkit.TestActorRef

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Success,Failure ,Try}
import scala.concurrent.ExecutionContext.Implicits.global


class Test extends FlatSpec with Matchers {

  "Table Of Content extractor" should "download and extract content from Oxford Site" in {
    val content:List[String] = OxfordSite.getTableOfContent
    content.size should be (10)
    content.find(_ == "A-B") should be (Some("A-B"))
    content.find(_ == "U-Z") should be (Some("U-Z"))
  }

  "Words list extractor" should "download words from page" in {
    val wordsTry:Try[Option[List[String]]] = OxfordSite.getWordsFromPage("A-B", 1)
    wordsTry should be a 'success
    val words = wordsTry.get
    words.get.find(_ == "abandon") should be (Some("abandon"))

  }
  "Words list extractor" should "return None from empty page" in {
    val wordsTry:Try[Option[List[String]]] = OxfordSite.getWordsFromPage("A-B", 999)
    wordsTry should be a 'success
    val words = wordsTry.get
    words should be(None)

  }

  "Russian Translation" should "download translation and parse" in {
    val page: Future[String] =  LingvoSite.getPage("test")
    val pageResult: String= Await.result(page,60 seconds)
    pageResult.contains("тест") should be(true)
    LingvoSite.parseTranslation(pageResult).get should be("тест")
  }



  "English Translation" should "download translation and parse" in {
    val page: Future[String] =  OxfordSite.getPage("test")
    val pageResult: String = Await.result(page,60 seconds)
    pageResult.contains("examination") should be(true)
    OxfordSite.parseTranslation(pageResult).get should be(("test", "an examination of somebody’s knowledge or ability, consisting of questions for them to answer or activities for them to perform"))

  }



}