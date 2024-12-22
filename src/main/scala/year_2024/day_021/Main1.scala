package year_2024.day_021

import year_2024.day_021.Key._


object Main1 extends App {

  val inputs = Parser.readInput(isSample = true)

  println(inputs.map(keys => (keyListToNumber(keys), dialNested(keys)))
  .map{
    case (numberFromKey, dialed) => numberFromKey * dialed.size
  }.sum)

  def dialNested(keys:List[Key]):List[Key] = {
    val possibilities =
      for {
        dial1 <- NumericKeypad.dial(keys)
        dial2 <- DirectionKeypad.dial(dial1)
        dial3 <- DirectionKeypad.dial(dial2)
      } yield dial3

    possibilities.minBy(_.size)
  }

}
