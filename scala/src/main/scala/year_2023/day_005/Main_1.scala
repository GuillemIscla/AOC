package year_2023.day_005

object Main_1 extends App {
  val (seedList, mapChain) = Parser.readInput(isSample = false)

  println(seedList.longList.map(mapChain.chainInput).min)

}
