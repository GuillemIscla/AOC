package year_2023.day_017


object Main_1 extends App {
  val heatLossMap:HeatLossMap = Parser.readInput(isSample = false)

  println(heatLossMap.getMinPath)
}
