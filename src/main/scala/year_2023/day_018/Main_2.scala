package year_2023.day_018



object Main_2 extends App {
  val digPlan: DivisibleDigPlan = Parser.readInput(isSample = None).fromHex

  val lavaLagoon = digPlan.dig.fill
  println(lavaLagoon.countInterior + lavaLagoon.countTrenches)

}
