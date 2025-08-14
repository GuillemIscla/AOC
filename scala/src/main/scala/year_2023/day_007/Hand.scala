package year_2023.day_007





case class Hand(cards:List[Card], bid:Int) {
  private val cardsMinusJoker:List[Card] = cards.filter(_ != Joker)
  private val jokers:Int = cards.count(_ == Joker)
  private val cardsCount:Map[Card, Int] = cardsMinusJoker.groupBy(c => c).view.mapValues(_.size).toMap

  def handType:Int = () match {
    case _ if cardsCount.values.count(c => c + jokers == 5) == 1 => 6 // Five of a kind
    case _ if cardsCount.values.count(c => c + jokers == 4) == 1 => 5 // Four of a kind
    case _ if cardsCount.values.count(c => c + jokers == 3) >= 1 => //Need to see where the joker combines or not to produce a Full house or just three of a kind
      jokers match {
        case 0 if cardsCount.values.count(c => c == 2) == 1 => 4 //Full house, no jokers
        case 0 => 3 //Three of a kind, no jokers
        case 1 if cardsCount.values.count(c => c + jokers == 3) == 2 => 4 //Full house, 1 joker that can combine with any of the two pairs
        case 1 => 3 //Three of a kind, just one joker and one pair
        case 2 => 3 //Three of a kind, two jokers and nothing else
        case _ => throw new Exception(s"Not expecting $jokers jokers")
      }
    case _ if cardsCount.values.count(c => c + jokers == 2) == 2 => 2 // Two pairs
    case _ if cardsCount.values.count(c => c + jokers == 2) == 1 => 1 // One pair
    case _ => // Cases where there was just one card (or zero) and may not be covered
      jokers match {
        case 5 => 6 //Five of a kind, all jokers
        case 4 => 6 //Five of a kind, 4 jokers + 1
        case 3 => 5 //Four of a kind, 4 jokers + 1
        case 2 => 3 //Three of a kind, 2 jokers + 1
        case 1 => 1 //One pair, 1 joker + 1
        case 0 => 0 //High card
      }
  }
}


object Hand {
  /**
   * Returns an integer whose sign communicates how x compares to y.
   * The result sign has the following meaning:
   * negative if x < y
   * positive if x > y
   * zero otherwise (if x == y)
   * */
  implicit val ordering:Ordering[Hand] = (x: Hand, y: Hand) => {
    if (x.handType > y.handType) 1
    else if (x.handType < y.handType) -1
    else {
      x.cards.zip(y.cards).map {
        case (xCard, yCard) =>
          if (xCard.value > yCard.value) 1
          else if (xCard.value < yCard.value) -1
          else 0
      }.find(_ != 0).getOrElse(0)
    }
  }
}
