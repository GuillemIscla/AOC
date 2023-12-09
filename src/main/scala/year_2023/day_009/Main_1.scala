package year_2023.day_009

object Main_1 extends App {
  println(Parser.readInput(isSample = false).map(_.getPrediction).sum)
}
