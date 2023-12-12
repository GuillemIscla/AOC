package year_2023.day_006

import scala.io.Source
import scala.util.Try

object Parser {

  def readInput(isSample:Boolean, hasKerning:Boolean = false):List[Race] = {
    if(isSample){
      parseInput("""Time:      7  15   30
                    Distance:  9  40  200""".stripMargin.split("\n").toList, hasKerning)
    }
    else parseInput(Source.fromResource("day_006_input.txt").getLines().toList, hasKerning)
  }

  def parseInput(input:List[String], hasKenring:Boolean):List[Race] = input match {
    case List(timesRaw, distancesRaw) =>
      val times = parseInts(timesRaw, hasKenring)
      val distances = parseInts(distancesRaw, hasKenring)
      times.zip(distances).map{
        case (time, distance) => Race(time, distance)
      }
    case other => throw new Exception(s"expecting two lines but got $other")
  }

  def parseInts(line:String, hasKerning:Boolean):List[Long] = {
    if(hasKerning)
      List(line.split(":")(1).replace(" ", "").toLong)
    else
      line.split(":")(1).split(" ").flatMap(_.toLongOption).toList
  }

}
