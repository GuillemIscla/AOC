package year_2024.day_001

object Main2 extends App {
  val input = Parser.readInput(isSample = false)

  val col1 = input.map(_._1)
  val col2 = input.map(_._2)

  println(col1.map(value => col2.count(_ == value) * value).sum)
}
