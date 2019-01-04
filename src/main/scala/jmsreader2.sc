import com.fasterxml.jackson.databind.{DeserializationFeature, JsonNode, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper


var testXml = """<USAGE type="INSERT" ts="2014-03-04 15:09:34">
  <SERVICE_ID>21036931</SERVICE_ID>
  <MSISDN>12</MSISDN>
  <CHARGE_ID>102146853871</CHARGE_ID>
  <NORMALISED_EVENT_ID>56755466389</NORMALISED_EVENT_ID>
  <UC_LABEL>105:101:101:110</UC_LABEL>
  <MAIN_UC_LABEL>105_110</MAIN_UC_LABEL>
  <SUB_UC_LABEL>105:101:101:110</SUB_UC_LABEL>
  <CHARGE_START_DATE>2014-02-19 22:47:55</CHARGE_START_DATE>
  <EVENT_DATE>2014-02-19 22:47:55</EVENT_DATE>
  <PERIOD_START_DATE>2014-02-01 00:00:00</PERIOD_START_DATE>
  <PERIOD_END_DATE>2014-02-28 23:59:59</PERIOD_END_DATE>
  <A_PARTY_ID>46763370205</A_PARTY_ID>
  <B_PARTY_ID>46763389217</B_PARTY_ID>
  <NETWORK_ID>24002</NETWORK_ID>
  <COUNTRY>SE</COUNTRY>
  <QTY>1</QTY>
  <QTY_UOM>COUNT</QTY_UOM>
  <AMT>0</AMT>
  <AMT_WITH_VAT>0</AMT_WITH_VAT>
  <DURATION>4148</DURATION>
  <INV_TXT><![CDATA[National and International Voice Calls]]></INV_TXT>
  <FM_CONSUMED TOTAL_AMT=".55" TOTAL_AMT_WITH_VAT=".69">
    <FM ALLOC_ID="70990600" FM_CATEGORY="8" AMT=".55" AMT_WITH_VAT=".69"/>
  </FM_CONSUMED>
  <FU_CONSUMED TOTAL_QTY="4148">
    <FU ALLOC_ID="75049690" QTY="4148"/>
  </FU_CONSUMED>
</USAGE>"""

//var expectedJson = '{"billingEvent" : {"action" : "INSERT", "columns" : {"FM_TOTAL_AMOUNT_WITH_VAT" : ".69", "NORMALISED_EVENT_ID" : "56755466389", "B_PARTY_ID" : "46763389217", "PERIOD_END_DATE" : "2014-02-28 23:59:59", "INV_TXT" : "National and International Voice Calls", "MAIN_UC_LABEL" : "105_110", "CHARGE_ID" : "102146853871", "EVENT_DATE" : "2014-02-19 22:47:55", "MSISDN" : "12", "FM_TOTAL_AMOUNT_WITHOUT_VAT" : ".55", "AMT" : "0", "A_PARTY_ID" : "46763370205", "SUB_UC_LABEL" : "105:101:101:110", "QTY" : "1", "NETWORK_ID" : "24002", "DURATION" : "4148", "PERIOD_START_DATE" : "2014-02-01 00:00:00", "QTY_UOM" : "COUNT", "AMT_WITH_VAT" : "0", "FU_CONSUMED" : "4148"}}}'

//var expectedJson2 = '{' +
//'"billingEvent" : ' +
//  '{' +
//'"action" : "INSERT", ' +
//'"columns" : ' +
//  '{' +
//'"FM_TOTAL_AMOUNT_WITH_VAT" : ".69", ' +
//'"NORMALISED_EVENT_ID" : "56755466389", ' +
//'"B_PARTY_ID" : "46763389217", ' +
//'"PERIOD_END_DATE" : "2014-02-28 23:59:59", ' +
//'"INV_TXT" : "National and International Voice Calls", ' +
//'"MAIN_UC_LABEL" : "105_110", ' +
//'"CHARGE_ID" : "102146853871", ' +
//'"EVENT_DATE" : "2014-02-19 22:47:55", ' +
//'"MSISDN" : "12", ' +
//'"FM_TOTAL_AMOUNT_WITHOUT_VAT" : ".55", ' +
//'"AMT" : "0", ' +
//'"A_PARTY_ID" : "46763370205", ' +
//'"SUB_UC_LABEL" : "105:101:101:110", ' +
//'"QTY" : "1", ' +
//'"NETWORK_ID" : "24002", ' +
//'"DURATION" : "4148", ' +
//'"PERIOD_START_DATE" : "2014-02-01 00:00:00", ' +
//'"QTY_UOM" : "COUNT", ' +
//'"AMT_WITH_VAT" : "0", ' +
//'"FU_CONSUMED" : "4148", ' +
//'"FU_CONSUMED_NEW" : {' +
//  '"TOTAL_QTY" : "4148", ' +
//  '"FU" : ' +
//  '[' +
//  '{' +
//  '"ALLOC_ID" : "75049690", ' +
//  '"QTY" : "4148" ' +
//  '}' +
//  ']' +
//  '}' +
//  '}' +
//  '}' +
//  '}'
//}

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

//case class Event(eventType: String,
//                 startDate : String,
//                 jsonNode: JsonNode)


class Column(val label: String) {
  override def toString(): String = "(label is: " + label + ")"
}

val col1 = new Column("FU_CONSUMED")
val col2 = new Column("FM_CONSUMED")

val columns = List(col1, col2)

//  '"ALLOC_ID" : "75049690", ' +
//  '"QTY" : "4148" ' +
var result = columns
  .flatMap { column =>
    if (column.label.startsWith("FU_CONSUMED")) {
      var freeUnits = Map(("ALLOC_ID" -> "75049690"), ("QTY" -> "4148"))
      List((column.label, Map("TOTAL_QTY" -> "4148", "FU" -> freeUnits)))
      //      List( (column.label, nodeValue((column \ "@TOTAL_QTY").find {
      //        node => true
      //      })))

    } else if (column.label.startsWith("FM_")) {
      List((column.label, "på"))
      //      List(("FM_TOTAL_AMOUNT_WITH_VAT", nodeValue((column \ "@TOTAL_AMT_WITH_VAT").find {
      //        node => true
      //      })),
      //        ("FM_TOTAL_AMOUNT_WITHOUT_VAT", nodeValue((column \ "@TOTAL_AMT").find {
      //          node => true
      //        })))
    } else {
      List((column.label, "dig"))
      //      List((column.label, column.text))
    }
  }.toMap

var action =  "INSERT"


var mapResult = Map("billingEvent" -> Map("action" -> action, "columns" -> result))

JsonUtil.toJsonNode(mapResult)
