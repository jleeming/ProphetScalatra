package prophet.scalatra
import org.scalatra.Initializable

// Инициализация приложения
trait MyInit extends Initializable {
   abstract override def initialize(config: Config) = {
     super.initialize(config)     
   }
}