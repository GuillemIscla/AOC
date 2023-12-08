package year_2023.day_003

object Main_1 extends App {

  val engineSchematic = Parser.readInput(isSample = false)
  println(engineSchematic.getNumbers().filter(engineSchematic.isAdjacentToSymbol).map(_.value).sum)

}
