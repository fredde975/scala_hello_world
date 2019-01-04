import com.fasterxml.jackson.databind.{DeserializationFeature, JsonNode, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper


object JsonUtil {
  val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)
  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

  def toJsonString(value: Map[Symbol, Any]): String = {
    toJsonString(value map { case (k, v) => k.name -> v })
  }

  def toJsonString(value: Any): String = {
    mapper.writeValueAsString(value)
  }

  def toJsonNode(value: Map[Symbol, Any]): JsonNode = {
    toJsonNode(value map { case (k, v) => k.name -> v })
  }

  def toJsonNode(value: Any): JsonNode = {
    mapper.readTree(mapper.writeValueAsString(value))
  }

  def toMap[V](json: String)(implicit m: Manifest[V]) = fromJson[Map[String, V]](json)

  def fromJson[T](json: String)(implicit m: Manifest[T]): T = {
    mapper.readValue[T](json)
  }

  def parseToJsonNode(jsonString: String): JsonNode = {
    mapper.readTree(jsonString)
  }
}


class Column(val label: String) {
  override def toString(): String = "(label is: " + label + ")"
}

val col1 = new Column("FU_CONSUMED")
val col2 = new Column("FM_CONSUMED")

val columns = List(col1, col2)

val result = columns
  .flatMap { column =>
    if (column.label.startsWith("FU_CONSUMED")) {
      val freeUnits = Map(("ALLOC_ID" -> "75049690"), ("QTY" -> "4148"))
      List((column.label + "_new", Map("TOTAL_QTY" -> "4148", "FU" -> freeUnits)),
        (column.label, "4148"))
    } else if (column.label.startsWith("FM_")) {
      List((column.label, "på"))
    } else {
      List((column.label, "dig"))
    }
  }.toMap

val action =  "INSERT"


val mapResult = Map("billingEvent" -> Map("action" -> action, "columns" -> result))

JsonUtil.toJsonNode(mapResult)

