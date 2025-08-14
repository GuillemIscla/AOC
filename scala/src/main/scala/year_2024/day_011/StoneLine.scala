package year_2024.day_011

case class Stone(num:Long) {
  def blink:List[Stone] = num match {
    case 0 => List(Stone(1))
    case digits if digits.toString.length % 2 == 0 =>
      val half = digits.toString.length / 2
      List(
        Stone(digits.toString.substring(0, half).toLong),
        Stone(digits.toString.substring(half).toLong)
      )
    case other => List(Stone(other * 2024))
  }
}

case class StoneLine(stones:List[Stone]){
  def blink:StoneLine =
    StoneLine(stones.flatMap(_.blink))

  def toWeightedStones:WeightedStones =
    WeightedStones(stones.map(s => (s, 1)))
}

case class WeightedStones(stonesAndWeights:List[(Stone, Long)]){
  def blink:WeightedStones =
    WeightedStones {
      stonesAndWeights.flatMap {
        case (stone, weight) =>
          stone.blink.map(s => (s, weight))
      }.groupBy(_._1).view.mapValues{
        stonesAndWeights => stonesAndWeights.map(_._2).sum
      }.toList
    }
}
