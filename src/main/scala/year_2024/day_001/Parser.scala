package year_2024.day_001

import scala.io.Source

object Parser {

  def readInput(isSample:Boolean):List[(Int, Int)] = {
    if(isSample){
      parseInput("""3   4
        |4   3
        |2   5
        |1   3
        |3   9
        |3   3""".stripMargin.split("\n").toList)
    }
    else {
      parseInput(Source.fromResource("year_2024/day_001_input.txt").getLines().toList)
    }
  }

  def parseInput(list:List[String]):List[(Int, Int)] = {
    val columnsUnsorted = list.map{
      line =>
        val arrayInts = line.split("   ").map(_.toInt)
        (arrayInts.head, arrayInts(1))
    }
    val col1 = columnsUnsorted.map(_._1).sorted
    val col2 = columnsUnsorted.map(_._2).sorted
    col1.zip(col2)

  }

}
