package year_2023.day_006

object Main_1 extends App {
  val races = Parser.readInput(isSample = false)

  println(races.map(_.numberOfWaysToWin).product)

}
