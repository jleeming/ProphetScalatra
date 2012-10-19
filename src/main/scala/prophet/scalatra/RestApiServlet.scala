package prophet.scalatra

import prophet._
import org.scalatra._
import org.scalatra.liftjson._
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import prophet.services.cache.Clear
import prophet.services.Prophet
import org.scalatra.scalate.ScalateSupport

case class User(name: String, age: Int)

class RestApiServlet extends ScalatraServlet with Initializable with LiftJsonSupport with ScalateSupport {
  before("/*") {
    contentType = "application/json;charset=UTF-8"
  }

  get("/currency/:currencyType") {
    params("currencyType") match {
      case currencyType => Prophet.forecast(currencyType.toUpperCase()): JValue
    }
  }
  
  delete("/cache") {
    System.cacheMaster ! Clear
    ("success" -> true) ~ ("message" -> "OK")
  }
  
//  error {
//    case e: Exception =>
//      // TODO: Use log instead of console
//      println("Unexpected error during http api call.", e)
//      status_=(500)
//      contentType = "application/json;charset=UTF-8"
//      compact(render(("message" -> "Internal error.")))
//  }
//
//  notFound {
//    contentType = "application/json;charset=UTF-8"
//    status_=(404)
//    compact(render(("message" -> "Not found.")))
//  }
  
  override def destroy() = {
    System.system.shutdown
    super.destroy()
  }
}
