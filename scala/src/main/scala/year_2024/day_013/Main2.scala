package year_2024.day_013



object Main2 extends App {

  val input = Parser.readInput(isSample = false).map{
    case ClawMachine(buttonA, buttonB, Prize(x, y)) =>
      ClawMachine(buttonA, buttonB, Prize(x + 10000000000000L, y + 10000000000000L))
  }


  val clawsStrategies = input.map(_.strategiesWithIntersecting).filter(_.nonEmpty)

  val bestClawsStrategies = clawsStrategies.map(_.minBy(_._3))

  println(bestClawsStrategies.map(_._3).sum)


}
