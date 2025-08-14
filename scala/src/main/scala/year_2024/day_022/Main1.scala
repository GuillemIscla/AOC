package year_2024.day_022

import Secret._

object Main1 extends App {

  val inputs = Parser.readInput(isSample = false)

  println(inputs.map(iterate).sum)

  def iterate(start:Long):Long = {
    (0 until 2000).foldLeft(start){
      case (acc, _) => allSteps(acc)
    }
  }

}
