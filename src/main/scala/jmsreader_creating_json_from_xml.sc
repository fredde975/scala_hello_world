import com.fasterxml.jackson.databind.{DeserializationFeature, JsonNode, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

import scala.xml.{Elem, Node, XML}


val testXml = """<USAGE type="INSERT" ts="2014-03-04 15:09:34">
  <PERIOD_START_DATE>2014-02-01 00:00:00</PERIOD_START_DATE>
  <FM_CONSUMED TOTAL_AMT=".55" TOTAL_AMT_WITH_VAT=".69">
    <FM ALLOC_ID="70990600" FM_CATEGORY="8" AMT=".55" AMT_WITH_VAT=".69"/>
  </FM_CONSUMED>
  <FU_CONSUMED TOTAL_QTY="4148">
    <FU ALLOC_ID="75049690" QTY="4148"/>
    <FU ALLOC_ID="001" QTY="999"/>
    <FU/>
  </FU_CONSUMED>
</USAGE>"""

//val expectedJson = '{"billingEvent" : {"action" : "INSERT", "columns" : {"FM_TOTAL_AMOUNT_WITH_VAT" : ".69", "NORMALISED_EVENT_ID" : "56755466389", "B_PARTY_ID" : "46763389217", "PERIOD_END_DATE" : "2014-02-28 23:59:59", "INV_TXT" : "National and International Voice Calls", "MAIN_UC_LABEL" : "105_110", "CHARGE_ID" : "102146853871", "EVENT_DATE" : "2014-02-19 22:47:55", "MSISDN" : "12", "FM_TOTAL_AMOUNT_WITHOUT_VAT" : ".55", "AMT" : "0", "A_PARTY_ID" : "46763370205", "SUB_UC_LABEL" : "105:101:101:110", "QTY" : "1", "NETWORK_ID" : "24002", "DURATION" : "4148", "PERIOD_START_DATE" : "2014-02-01 00:00:00", "QTY_UOM" : "COUNT", "AMT_WITH_VAT" : "0", "FU_CONSUMED" : "4148"}}}'

//val expectedJson2 = '{' +
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

case class Event(eventType: String,
                 startDate : String,
                 jsonNode: JsonNode)

val columnFilter = List("A_PARTY_ID", "B_PARTY_ID", "CHARGE_ID", "EVENT_DATE", "DURATION", "MAIN_UC_LABEL", "MSISDN", "PERIOD_END_DATE", "PERIOD_START_DATE", "SUB_UC_LABEL", "NORMALISED_EVENT_ID", "QTY", "QTY_UOM", "AMT", "AMT_WITH_VAT", "INV_TXT", "FM_CONSUMED", "FU_CONSUMED", "NETWORK_ID")

def nodeValue(node: Option[Node]): String = {
  node match {
    case Some(node: Node) => node.text
    case None => ""
  }
}

val xml = XML.loadString(testXml)
val event = xmlToJSON(testXml)

def handleBillingEvent(xml: Elem): Event = {
  println("Handling Billing XML: " + xml + " \n")
  val action = (xml \ "@type").text
  val billPeriodStartDate = (xml \ "PERIOD_START_DATE").text
  println("Bill period start date is: " + billPeriodStartDate + "\n")
  val columns = JsonUtil.toJsonNode((xml \ "_").filter {
    column =>
      columnFilter.contains(column.label)
  }.flatMap {
    column =>
      println("This is column: "+ column + "\n\n")
      if (column.label.startsWith("FU_CONSUMED")) {
//        val freeUnits = Map(("ALLOC_ID" -> "75049690"), ("QTY" -> "4148"))
//        List((column.label + "_new", Map("TOTAL_QTY" -> "4148", "FU" -> freeUnits)),
//          (column.label, "4148"))
        val children = column.child
        print("Number of children: " + children.length + "\n\n")
        print("These are the children: " + children + "\n\n")
        val freeUnits = children.map(child =>
          if(child.label.startsWith("FU")){
            print("This is a child: " + child + "\n")
            val qty = (child \ "@QTY").find {node => true}.getOrElse("Value not set")
            val allocId = (child \ "@ALLOC_ID").find {node => true}.getOrElse("Value not set")

            print("ALLOC_ID = "  + allocId + "\n")
            print("QTY = " + qty + "\n")
            Some(Map(("ALLOC_ID" -> allocId.toString), ("QTY" -> qty.toString)))
          }else{
            None
          })
          .flatten

        print("This is the list of freeUnits: " + freeUnits + "\n\n")
        val totalQty = nodeValue((column \ "@TOTAL_QTY").find {node => true})

        val fakeUnits = List(Map("a" -> "b", "c" -> "d"), Map("a" -> "b", "c" -> "d"))
        print("This is the fake freeUnits: " + fakeUnits + "\n\n")

        List(
//          (column.label + "_NEW", Map("TOTAL_QTY" -> totalQty, "FU" -> freeUnits))
          (column.label + "_new", Map("TOTAL_QTY" -> "4148", "FU" -> freeUnits)),
          (column.label, totalQty))

      } else if (column.label.startsWith("FM_")) {
        List(("FM_TOTAL_AMOUNT_WITH_VAT", nodeValue((column \ "@TOTAL_AMT_WITH_VAT").find {
          node => true
        })),
          ("FM_TOTAL_AMOUNT_WITHOUT_VAT", nodeValue((column \ "@TOTAL_AMT").find {
            node => true
          })))
      } else {
        List((column.label, column.text))
      }
  }.toMap)

  val mapResult = Map("billingEvent" -> Map("action" -> action, "columns" -> columns))
  JsonUtil.toJsonNode(mapResult)
  Event("BillingEvent" + action, billPeriodStartDate, JsonUtil.toJsonNode(Map("billingEvent" -> Map("action" -> action, "columns" -> columns))))
}

def xmlToJSON(jsonEvent: String): Event = {
  val xml = XML.loadString(jsonEvent)
  xml match {
    case <USAGE>{_*}</USAGE> =>
      println("This is USAGE \n")
      handleBillingEvent(xml)
    case _ =>
      println("Unsupported XML: \n" + xml)
      null
  }
}

