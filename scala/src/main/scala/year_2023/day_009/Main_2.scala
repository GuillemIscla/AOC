package year_2023.day_009

object Main_2 extends App {
  println(Parser.readInput(isSample = false).map(_.getExtrapolation).sum)
}
