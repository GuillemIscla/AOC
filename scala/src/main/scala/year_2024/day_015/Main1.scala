package year_2024.day_015

object Main1 extends App {

  val (warehouse, movements) = Parser.readInput(isSample = None)

  val finalWarehouse = movements.foldLeft(warehouse){
    case (accWarehouse, newMovement) =>
      accWarehouse.move(newMovement, wide = false)
  }

  println(finalWarehouse.gpsCoordinates.map(_._3).sum)
}
