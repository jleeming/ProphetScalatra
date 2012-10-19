package prophet

import akka.actor.ActorSystem
import prophet.services.cache.CacheMaster

object System {
  val system = ActorSystem("actorSystem")
  val cacheMaster = system.actorOf(akka.actor.Props[CacheMaster], name = "cacheMaster")
}