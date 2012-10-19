package prophet.scalatra

import org.scalatra.Initializable

// Инициализация приложения
trait MyInit extends Initializable {
   abstract override def initialize(config: ConfigT) = {
     super.initialize(config)     
   }
}