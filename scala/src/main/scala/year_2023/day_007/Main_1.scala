package year_2023.day_007

import Hand._

object Main_1 extends App {
  val hands = Parser.readInput(isSample = false )

  println(hands.sorted.zipWithIndex.map{
    case (hand, index) => hand.bid * (1 + index)
  }.sum)

}
