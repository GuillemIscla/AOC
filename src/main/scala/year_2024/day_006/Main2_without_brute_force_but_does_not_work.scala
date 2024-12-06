package year_2024.day_006

object Main2_without_brute_force_but_does_not_work extends App {
  val map = Parser.readInput(isSample = true)

  val rowsNum = map.getRowsNum

  println((0 until rowsNum).flatMap {
    row =>
      map.getObstructionsRow(row).flatMap(getLoops)
  }.size)

  println((0 until rowsNum).flatMap {
    row =>
      map.getObstructionsRow(row).flatMap(getLoops)
  })

  def getLoops(obstruction1: (Int, Int)): List[((Int, Int), (Int, Int), (Int, Int), (Int, Int))] = {
    val (i_1, j_1) = obstruction1
    if (obstruction1 == (0, 4)) {
      println()
    }
    if (i_1 == rowsNum - 1) List.empty
    else {
      map.getObstructionsRow(i_1 + 1).flatMap {
        case (i_2, j_2) if j_2 > j_1 =>
          if (j_2 - 1 < 0) List.empty
          else {
            map.getObstructionsCol(j_2 - 1).flatMap {
              case (i_3, j_3) if i_3 > i_2 =>
                val (i_4, j_4) = (i_3 - 1, j_1 - 1)

                if (i_3 < 0 || i_3 > map.width || i_4 < 0 || i_4 > map.width ||
                  j_3 < 0 || j_3 > map.height || j_4 < 0 || j_4 > map.height) None
                else if (map.raw(i_3)(j_3) == '#') {
                  Some(((i_1, j_1), (i_2, j_2), (i_3, j_3), (i_4, j_4)))
                }
                else None
              case _ => None
            }
          }
        case _ => List.empty
      }
    }
  }

}
