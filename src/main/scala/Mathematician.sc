def square(x: Int) = x * x

square(2)

1.to(10)

"Hello, Scala!".size

1 + 2

class Point(x: Int, y: Int) {
  override def toString(): String = "(" + x + ", " + y + ")"
}

val pt = new Point(1, 2)
println(pt)

print(new Point(2,2))

class ClassWithValParameter(val name: String)

val aClass = new ClassWithValParameter("Gandalf")
print(aClass.name)

def maybeItWillReturnSomething(flag: Boolean): Option[String] = {
  if (flag) Some("Found value") else None
}

val value1 = maybeItWillReturnSomething(true)
val value2 = maybeItWillReturnSomething(false)



value1 getOrElse( "No Value")
value2 getOrElse( "No Value")

print(value1)
print(value2)


val someValue: Option[Double] = Some(20.0)
val value = someValue match {
  case Some(v) ⇒ v
  case None ⇒ 0.0
}

print("SomeValue is: " + value)

val number: Option[Int] = Some(3)
val noNumber: Option[Int] = None
val result1 = number.map(_ * 1.5)
val result2 = noNumber.map(_ * 1.5)


val number1: Option[Int] = Some(3)
val noNumber1: Option[Int] = None
val result11 = number.fold(1)(_ * 3)
val result21 = noNumber.fold(1)(_ * 3)

object Greeting {
  def english = "Hi"
  def espanol = "Hola"
  def swedish = None
}

Greeting.english
Greeting.espanol
Greeting.swedish

val x = Greeting
val y = x

val z = Greeting

x eq(z)


class Movie(val name: String, val year: Short)

object Movie {
  def academyAwardBestMoviesForYear(x: Short) = {
    //This is a match statement, more powerful than a Java switch statement!
    x match {
      case 1930 ⇒ Some(new Movie("All Quiet On the Western Front", 1930))
      case 1931 ⇒ Some(new Movie("Cimarron", 1931))
      case 1932 ⇒ Some(new Movie("Grand Hotel", 1932))
      case _ ⇒ None
    }
  }
}

Movie.academyAwardBestMoviesForYear(1932).get.year


class Person(val name: String, private val superheroName: String) //The superhero name is private!

object Person {
  def showMeInnerSecret(x: Person) = x.superheroName
}

val clark = new Person("Clark Kent", "Superman")
val peter = new Person("Peter Parker", "Spider-Man")

clark.name
Person.showMeInnerSecret(clark)