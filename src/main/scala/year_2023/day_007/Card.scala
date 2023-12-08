package year_2023.day_007

trait Card {
  def raw:Char
  def value:Int
}

object Card {
  val allMinusJoker:List[Card] = List(A, K, Q, J, T, N9, N8, N7, N6, N5, N4, N3, N2)
  def fromRaw(withJoker:Boolean)(raw:Char):Card =
    if(withJoker && raw == 'J') Joker
    else allMinusJoker.find(_.raw == raw).getOrElse(throw new Exception(s"Could not parse card with character '$raw'"))
}

case object A extends Card {
  override def raw: Char = 'A'

  override def value: Int = 14
}

case object K extends Card {
  override def raw: Char = 'K'

  override def value: Int = 13
}

case object Q extends Card {
  override def raw: Char = 'Q'

  override def value: Int = 12
}

case object J extends Card {
  override def raw: Char = 'J'

  override def value: Int = 11
}

case object T extends Card {
  override def raw: Char = 'T'

  override def value: Int = 10
}

case object N9 extends Card {
  override def raw: Char = '9'

  override def value: Int = 9
}
case object N8 extends Card {
  override def raw: Char = '8'

  override def value: Int = 8
}

case object N7 extends Card {
  override def raw: Char = '7'

  override def value: Int = 7
}

case object N6 extends Card {
  override def raw: Char = '6'

  override def value: Int = 6
}

case object N5 extends Card {
  override def raw: Char = '5'

  override def value: Int = 5
}

case object N4 extends Card {
  override def raw: Char = '4'

  override def value: Int = 4
}

case object N3 extends Card {
  override def raw: Char = '3'

  override def value: Int = 3
}

case object N2 extends Card {
  override def raw: Char = '2'

  override def value: Int = 2
}

case object Joker extends Card {
  override def raw: Char = 'J'

  override def value: Int = 1
}
