package year_2023.day_021


/**
 * Examination is the code reveals we can write simple arithmetical formulas to resolve some questions
 * which are calculable frm stats
 * */

object Main_2 extends App {

  var garden = Parser.readInput(isSample = None)

  val createStatsWith = 700
  val stats = Iterator.getStats(garden.start, garden, stepsLeftToday = createStatsWith)

  val stepsLeftToday = 26501365L

  println(stats.result(stepsLeftToday))
}
