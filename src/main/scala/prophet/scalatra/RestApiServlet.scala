package prophet.scalatra

import prophet._
import org.scalatra._
import scalate.ScalateSupport
import net.liftweb.json._
import net.liftweb.json.JsonDSL._
import prophet.services.cache.Clear
import prophet.services.Prophet

case class User(name: String, age: Int)

class RestApiServlet extends ScalatraServlet with ScalateSupport with JsonHelpers with Initializable {
  before("/*") {
    contentType = "application/json;charset=UTF-8"
  }

  get("/currency/:currencyType") {
    params("currencyType") match {
      case currencyType => Json(Prophet.forecast(currencyType.toUpperCase()))  
        //Json(User("test " + currencyType, 30))
    }
  }
  
  get("/cache") {
    System.cacheMaster ! Clear
    ("success" -> true) ~ ("message" -> "OK")
  }
  
  
  get("/api/json/users/:id") {
    params("id") match {
      case "1" => Json(User("John", 30))
    }
  }

  post("/api/user") {
    val user = parse(request.body).extract[User]
    1
  }

  get("/error") {
    throw new RuntimeException("oh noez")
  }

  error {
    case e: Exception =>
      // TODO: Use log instead of console
      println("Unexpected error during http api call.", e)
      status(500)
      contentType = "application/json;charset=UTF-8"
      compact(render(("message" -> "Internal error.")))
  }

  notFound {
    contentType = "application/json;charset=UTF-8"
    status(404)
    compact(render(("message" -> "Not found.")))
  }
  
  override def destroy() = {
    System.system.shutdown
    super.destroy()
  }
}
