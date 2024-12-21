package year_2024.day_021

import scala.io.Source

object Parser {

  def readInput(isSample:Boolean):List[List[Key]] = {
    if(isSample){
      parseInput("""029A
                   |980A
                   |179A
                   |456A
                   |379A""".stripMargin.split("\n").toList)
    }
    else {
      parseInput(Source.fromResource("year_2024/day_021_input.txt").getLines().toList)
    }
  }

  def parseInput(lines: List[String]):List[List[Key]] =
    lines.map(_.toList.map(_.toString).map(Key.fromChar))



}
