package year_2023.day_007

import year_2023.day_007.Hand._

object Main_2 extends App {
  val hands = Parser.readInput(isSample = false, withJoker = true)

  println(hands.sorted.zipWithIndex.map {
    case (hand, index) =>
      hand.bid * (1 + index)
  }.sum)

}
