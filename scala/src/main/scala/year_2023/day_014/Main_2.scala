package year_2023.day_014

object Main_2 extends App {
  val rockField = Parser.readInput(isSample = false)

  println(rockField.cicle(1000000000).calculateLoad)
}
