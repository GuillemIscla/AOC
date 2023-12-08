package year_2023.day_006

object Main_2 extends App {
  val races = Parser.readInput(isSample = false, hasKerning = true)

  println(races.map(_.numberOfWaysToWin).product)

}
