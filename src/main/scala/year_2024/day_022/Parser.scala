package year_2024.day_022

import scala.io.Source

object Parser {

  def readInput(isSample:Boolean):List[Long] = {
    if(isSample){
      parseInput("""1
                   |2
                   |3
                   |2024
                   |""".stripMargin.split("\n").toList)
    }
    else {
      parseInput(Source.fromResource("year_2024/day_022_input.txt").getLines().toList)
    }
  }

  def parseInput(lines: List[String]):List[Long] = lines.map(_.toLong)



}
