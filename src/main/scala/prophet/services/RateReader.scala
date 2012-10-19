package prophet.services

import prophet._
import prophet.services.cache.CacheManager
import prophet.services.cache.CacheMaster
import prophet.services.cache.GetInfo
import prophet.services.cache.SetInfo

import net.liftweb.json.JsonAST.JValue
import net.liftweb.json.JsonAST.JString
import scala.io.Source
import scala.io.Codec
import scala.io.BufferedSource
import scala.collection.immutable.HashMap
import scala.xml._
import java.util.Date
import java.text.SimpleDateFormat
import prophet.model.Currency
import akka.actor._
import akka.dispatch.Await
import akka.pattern.ask
import org.joda.time._

class RateReader {
  val DAY_COUNT = 7
  val currencyTypeCode: HashMap[String, String] = HashMap(("USD" -> "R01235"), ("EUR" -> "R01239"), ("GBP" -> "R01035"))

  def readRatesWithCache(currencyType: String): Seq[Currency] = {
    val cacheMaster = System.cacheMaster
    
    var result: Seq[Currency] = null
    var i: Int = 0
    
    while ((i < 2) && (result == null)) { 
      try {
        implicit val timeout = akka.util.Timeout(500)
        val future = cacheMaster ? GetInfo(generateKey(currencyType)) // enabled by the “ask” import
        result = Await.result(future, timeout.duration).asInstanceOf[Seq[Currency]]
      } catch {
        case e: Exception =>
      }
      i += 1
    }  
    
    if (result == null) {
      println(" --- CBR call ---")
      val rates = readRates(currencyType)
      cacheMaster ! SetInfo(generateKey(currencyType), rates)
      rates
    } else {
      result
    }
  }
  
  private def generateKey(currencyType: String): String = {
    currencyType + ":" + String.format("%1$td.%1$tm.%1$tY", DateTime.now().toDate) + ":" + DAY_COUNT
  }
  
  def readRates(currencyType: String): Seq[Currency] = {
    val history = readRates(currencyType, DateTime.now().minus(Period.days(DAY_COUNT * 2)).toDate(), DateTime.now().toDate)
    if (history.size >= DAY_COUNT) history.slice(history.size - DAY_COUNT, history.size) else readRates(currencyType, DateTime.now().minus(Period.days(30)).toDate, DateTime.now().toDate).slice(history.size - DAY_COUNT, history.size)
  }

  private def readRates(currencyType: String, beginDate: Date, endDate: Date): Seq[Currency] = {
    val history = XML.load("""http://www.cbr.ru/scripts/XML_dynamic.asp?date_req1=""" + String.format("%1$td/%1$tm/%1$tY", beginDate) + """&date_req2=""" + String.format("%1$td/%1$tm/%1$tY", endDate) + """&VAL_NM_RQ=""" + currencyTypeCode.get(currencyType).get)
    val result: Seq[Currency] = (history \ "Record").map { record => { Currency(new java.text.SimpleDateFormat("dd.MM.yyyy").parse((record \ "@Date").text), BigDecimal((record \ "Value").text.replace(",", "."))) } }
    result
  }
}