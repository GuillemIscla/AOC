package year_2023.day_012

object Main_1 extends App {
  val hotSpringRecords = Parser.readInput(isSample = false)

  println(hotSpringRecords.map(_.arrangements.total).sum)


}
