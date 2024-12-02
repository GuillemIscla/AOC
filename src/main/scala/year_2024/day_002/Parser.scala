package year_2024.day_002

import scala.io.Source

object Parser {

  def readInput(isSample:Boolean):List[List[Int]] = {
    if(isSample){
      """7 6 4 2 1
                   |1 2 7 8 9
                   |9 7 6 2 1
                   |1 3 2 4 5
                   |8 6 4 4 1
                   |1 3 6 7 9""".stripMargin.split("\n").toList.map(parseInput)
    }
    else {
      Source.fromResource("year_2024/day_002_input.txt").getLines().toList.map(parseInput)
    }
  }

  def parseInput(line:String):List[Int] = {
    line.split(" ").map(_.toInt).toList
  }

}
