package prophet.model.serializer

import net.liftweb.json._
import java.text.SimpleDateFormat
import java.util.Date

/*
*  Serializer and Deserializers for Date
*/
object DateTimeSerializer extends Serializer[Date] {

    private val Class = classOf[Date]

    //  Deserializer Function for java.util.Date
    def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), Date] = {
        case (TypeInfo(Class, _), json) => json match {
            case JString(s) => new java.text.SimpleDateFormat("dd.MM.yyyy").parse(s)
            case x => throw new MappingException("Can't convert " + x + " to Date")
        }

    }

    def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
        case x: Date => JString(String.format("%1$td.%1$tm.%1$tY", x))
    }
}
