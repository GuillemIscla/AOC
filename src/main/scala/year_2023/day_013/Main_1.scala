package year_2023.day_013

object Main_1 extends App {
  val mirrorValleys = Parser.readInput(isSample = false)
  println(mirrorValleys.map(_.summarize).sum)
}
