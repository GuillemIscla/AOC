package year_2023.day_014

object Main_1 extends App {
  val rockField = Parser.readInput(isSample = false)
  println(rockField.tiltNorth.calculateLoad)
}
