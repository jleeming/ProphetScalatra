package prophet.model

import net.liftweb.json._
import scala.xml.Node
import java.util.Date
import prophet.model.serializer.BigDecimalSerializer
import prophet.model.serializer.DateTimeSerializer
import java.util.UUID
import org.joda.time._

case class CurrencyForecast(date: Date, currency: String, guid: String, history: Seq[Currency], future: Currency) {
  def this(currencyType: String, rates: Seq[Currency], forecast: BigDecimal) = this(DateTime.now().toDate(), currencyType, UUID.randomUUID().toString(), rates, Currency(DateTime.now.plus(Period.days(1)).toDate, forecast))
}

object CurrencyForecast {
  private implicit val formats =
    net.liftweb.json.DefaultFormats + BigDecimalSerializer + DateTimeSerializer

  /**
   * Convert the item to JSON format.  This is
   * implicit and in the companion object, so
   * an CurrencyForecast can be returned easily from a JSON call
   */
  implicit def toJson(cf: CurrencyForecast): JValue =
    Extraction.decompose(cf)
}