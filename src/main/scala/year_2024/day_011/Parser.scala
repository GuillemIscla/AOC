package year_2024.day_011

import scala.io.Source

object Parser {

  def readInput(isSample:Boolean):StoneLine = {
    if(isSample){
      parseInput("""1""".stripMargin)
    }
    else {
      parseInput(Source.fromResource("year_2024/day_011_input.txt").getLines().toList.head)
    }
  }

  def parseInput(line: String):StoneLine =
    StoneLine(line.split(" ").map(_.toLong).map(Stone.apply).toList)



}
