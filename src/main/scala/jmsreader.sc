class Column(val label: String) {
  override def toString(): String = "(label is: " + label + ")"
}

val col1 = new Column("FU_CONSUMED")
val col2 = new Column("FM_CONSUMED")


val columns = List(col1, col2)
columns.map( col => print( col))


def makeUpper(xs: List[String]) = xs map {
  _.toUpperCase
}

columns.map( col => col)


val values = List(("a",1), ("b",2), ("c",3))
  .toMap

val values2 = List(("a",1))
  .toMap



val list = List(1, 2, 3)

list.map(x => x * 2)


def g(y: Int) = List(y * y, y + y, y - y)

list.map(x => g(x))

list.flatMap(x => g(x))


columns.map(column => println(column.label))

columns
  .map(_.label.toLowerCase())
  .flatMap(_.toUpperCase)

columns
  .flatMap(_.label.toLowerCase())


var result = columns
  .flatMap { column =>
    if (column.label.startsWith("FU_CONSUMED")) {
      List((column.label, "hej"))
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
