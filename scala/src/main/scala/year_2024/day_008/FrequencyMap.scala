package year_2024.day_008

case class FrequencyMap(raw:List[List[Char]]){

  val width: Int = raw.head.size
  val height: Int = raw.size
  def getAntennas:Map[Char, List[(Int, Int)]] = {
    val positionList =
      for {
        (row, i) <- raw.zipWithIndex
        (char, j) <- row.zipWithIndex
      } yield (char, i, j)

    positionList
      .filter(_._1 != '.')
      .groupBy(_._1)
      .view
      .mapValues(
        _.map{ case (_, i ,j) => (i,j) }
      ).toMap
  }

  def printWithAntinodes(antinodes:List[(Int, Int)]):Unit = {
    (0 until width).foreach{
      i =>
        (0 until height).foreach{
          j =>
            if(antinodes.contains(i,j)) print('#')
            else print(raw(i)(j))
        }
        println()
    }
  }
}
