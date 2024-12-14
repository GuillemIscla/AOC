package year_2024.day_013


object Main1 extends App {

  val input = Parser.readInput(isSample = false)

  val clawsStrategies = input.map(_.strategies(100)).filter(_.nonEmpty)

  val bestClawsStrategies = clawsStrategies.map(_.minBy(_._3))


  println(bestClawsStrategies.map(_._3).sum)


}
