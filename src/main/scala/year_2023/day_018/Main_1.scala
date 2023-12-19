package year_2023.day_018

object Main_1 extends App {
  val digPlan:DigPlan = Parser.readInput(isSample = None)
  val lavaLagoon:LavaLagoon = digPlan.dig

  println(lavaLagoon.fill.countTrenches)
}
