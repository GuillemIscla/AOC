package year_2024.day_021

import year_2024.day_021.Key._


object Main2 extends App {


  val inputs = Parser.readInput(isSample = false)

  println(inputs.map(keys => (keyListToNumber(keys), dialNestedCount(keys)))
    .map {
      case (keyFromNumber, dialedCount) => keyFromNumber * dialedCount
    }.sum)

  def dialNestedCount(keys: List[Key]):Long = {
    val possibilities =
      for {
        dial1 <- NumericKeypad.dial(keys)
      } yield DirectionKeypad.dialListCount(dial1, 25)
    possibilities.min
  }


}
