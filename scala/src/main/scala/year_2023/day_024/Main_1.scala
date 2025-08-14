package year_2023.day_024

object Main_1 extends App {
  val isSample = false
  val hailstoneList = Parser.readInput(isSample)

  val (lowerBoundary, upperBoundary) =
    if(isSample) (7.0, 27.0)
    else (200000000000000.0, 400000000000000.0)


  val intersections = hailstoneList.indices.flatMap {
    i =>
      (i + 1 until hailstoneList.size).map{
        j =>
          AlgebraService.getSolutions(hailstoneList(i), hailstoneList(j)) match {
            case Some((x, y)) =>
              x >= lowerBoundary &&
                x <= upperBoundary &&
                y >= lowerBoundary &&
                y <= upperBoundary
            case None =>
              false
          }
      }
  }.count(_ == true)

  println(intersections)




}
