package year_2023.day_015

object Main_1 extends App {
  val input = Parser.readInput(isSample = false)

  println(input.map(ASCII_Transformer.transformLabel))
}
