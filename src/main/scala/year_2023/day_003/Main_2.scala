package year_2023.day_003

object Main_2 extends App {

  val engineSchematic = Parser.readInput(isSample = false)
  println(engineSchematic.getGears().map(_.ratio).sum)

}
