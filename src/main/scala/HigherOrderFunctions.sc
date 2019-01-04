def lambda = { x: Int ⇒
  x + 1
}

def lambda2 = (x: Int) ⇒ x + 2
val lambda3 = (x: Int) ⇒ x + 3

val lambda4 = new Function1[Int, Int] {
  def apply(v1: Int): Int = v1 - 1
}

def lambda5(x: Int) = x + 1


val result = lambda(3)
val `result1andhalf` = lambda.apply(3)
val result2 = lambda2(3)
val result3 = lambda3(3)
val result4 = lambda4(3)
val result5 = lambda5(3)


def lambda1 = (x: Int) ⇒ x + 1
def result1 = lambda1(5)
print(result)

val multiplier = (i: Int) => i * 10

print(5 * multiplier(5) )


var incrementer = 1

def closure = { x: Int =>
  x + incrementer
}

val result6 = closure(10)

incrementer = 2
val result7 = closure(10)



def summation(x: Int, y: Int ⇒ Int) = y(x * 3)

var incrementer3 = 3

def closure3 = (x: Int) ⇒ x + incrementer3

summation(5, closure3)


def addWithoutSyntaxSugar(x: Int): Function1[Int, Int] = {
  new Function1[Int, Int]() {
    def apply(y: Int): Int = x + y
  }
}

addWithoutSyntaxSugar(1).isInstanceOf[Function1[Int, Int]]

def fiveAdder: Function1[Int, Int] = addWithoutSyntaxSugar(5)

fiveAdder(4)
addWithoutSyntaxSugar(5)(4)





def addWithSyntaxSugar(x: Int) = (y: Int) ⇒ x + y

addWithSyntaxSugar(1).isInstanceOf[Function1[Int, Int]]

addWithSyntaxSugar(2)(3)

def sixAdder = addWithSyntaxSugar(6)

sixAdder(5)



def addWithSyntaxSugar2(x: Int) = (y: Int) ⇒ x + y
addWithSyntaxSugar2(1).isInstanceOf[Function1[_, _]]


def makeUpper(xs: List[String]) = xs map {
  _.toUpperCase
}

def makeWhatEverYouLike(xs: List[String], sideEffect: String ⇒ String) =
  xs map sideEffect


makeUpper(List("Hej", "På", "Dig", "123"))

makeWhatEverYouLike(List("ABC", "XYZ", "123"), { x ⇒
  x.toLowerCase
})


val myName = (name: String) => s"My name is $name"

makeWhatEverYouLike(List("John", "Mark"), myName)



