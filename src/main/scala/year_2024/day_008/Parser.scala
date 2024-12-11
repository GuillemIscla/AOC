package year_2024.day_008

import scala.collection.mutable
import scala.io.Source

object Parser {

  def readInput(isSample:Boolean):FrequencyMap = {
    if(isSample ){
      parseInput(
        """............
          |........1...
          |.....1......
          |.......1....
          |....1.......
          |......A.....
          |............
          |............
          |........A...
          |.........A..
          |............
          |............
          |""".stripMargin.split("\n").toList)
    }
    else {
      parseInput(Source.fromResource("year_2024/day_008_input.txt").getLines().toList)
    }
  }

  def parseInput(lines: List[String]):FrequencyMap = {
    FrequencyMap(
      lines.map(_.toArray.toList)
    )
  }



}
