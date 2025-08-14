package year_2023.day_017

object Main_2 extends App {
  val ultraHeatLossMap: UltraHeatLossMap = Parser.readInput(isSample = false).toUltra

  println(ultraHeatLossMap.getMinPath)
}
