package com.igumnov.top3000words

import java.util.concurrent.Executors

import net.ruippeixotog.scalascraper.browser.Browser
import net.ruippeixotog.scalascraper.scraper.ContentExtractors._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._

import org.jsoup.Jsoup
import org.jsoup.nodes.Element

import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source
import scala.util.Try

object LingvoSite {

  def parseTranslation(content: String): Try[String] = {
    Try {
      val browser = new Browser
      val doc = browser.parseString(content)
      val spanElement: Element = doc >> element(".r_rs")
      spanElement >> text("a")
    }
  }

  def getPage(word: String): Try[String] = {
    Try {
      val html = Source.fromURL("http://www.translate.ru/dictionary/en-ru/" + java.net.URLEncoder.encode(word, "UTF-8"))
      val ret = html.mkString
      html.close()
      ret
    }
  }

}
