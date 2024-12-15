package year_2024.day_015

//1442232 too high
//1432781

object Main2 extends App {

  val (warehouse, movements) = Parser.readInput(isSample = None)

  val finalWarehouse = movements.foldLeft(warehouse.toWide) {
    case (accWarehouse, newMovement) =>
      accWarehouse.move(newMovement, wide = true)
  }

  println(finalWarehouse.gpsCoordinates.map(_._3).sum)

}
