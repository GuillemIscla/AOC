package year_2024.day_014

import scala.io.Source

object Parser {

  def readInput(isSample:Boolean):List[BathroomRobot] = {
    if(isSample){
      parseInput(11,7)("""p=0,4 v=3,-3
                   |p=6,3 v=-1,-3
                   |p=10,3 v=-1,2
                   |p=2,0 v=2,-1
                   |p=0,0 v=1,3
                   |p=3,0 v=-2,-2
                   |p=7,6 v=-1,-3
                   |p=3,0 v=-1,-2
                   |p=9,3 v=2,3
                   |p=7,3 v=-1,2
                   |p=2,4 v=2,-3
                   |p=9,5 v=-3,-3""".stripMargin.split("\n").toList)
    }
    else {
      parseInput(101, 103)(Source.fromResource("year_2024/day_014_input.txt").getLines().toList)
    }
  }

  def parseInput(width:Int, height:Int)(lines: List[String]):List[BathroomRobot] = {
    val robotRegex = "p=([\\-?0-9]+),(\\-?[0-9]+) v=(\\-?[0-9]+),(\\-?[0-9]+)".r
    lines.flatMap(robotRegex.findFirstMatchIn).map(
      patternMatch => BathroomRobot(width, height, patternMatch.group(1).toInt, patternMatch.group(2).toInt, patternMatch.group(3).toInt, patternMatch.group(4).toInt)
    )
  }


}
