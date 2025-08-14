package year_2023.day_007

import scala.io.Source

object Parser {

  def readInput(isSample:Boolean, withJoker:Boolean = false):List[Hand] = {
    if (isSample) {
        """32T3K 765
          T55J5 684
          KK677 28
          KTJJT 220
          QQQJA 483""".stripMargin.split("\n").toList.map(parseHand(withJoker))
    }
    else Source.fromResource("year_2023/day_007_input.txt").getLines().toList.map(parseHand(withJoker))
  }

  def parseHand(withJoker:Boolean)(value: String):Hand = {
    val (handRaw, bid) = {
      val array = value.split(" ").filter(s => !s.contains(" ")).filter(_.nonEmpty)
      (array(0), array(1))
    }

    Hand(handRaw.toArray.map(Card.fromRaw(withJoker)).toList, bid.toInt)
  }

}
