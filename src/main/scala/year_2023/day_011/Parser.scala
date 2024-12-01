package year_2023.day_011

import scala.io.Source

object Parser {

  def readInput(isSample:Boolean):UniverseMap = {
    if (isSample) {
      UniverseMap(parseRawMap("""...#......
      .......#..
      #.........
      ..........
      ......#...
      .#........
      .........#
      ..........
      .......#..
      #...#.....
""".split("\n").toList))
    }
    else UniverseMap(parseRawMap(Source.fromResource("year_2023/day_011_input.txt").getLines().toList))
  }

  def parseRawMap(lines:List[String]):List[List[UniverseObject]] =
    lines.map{
      line => line.toList.flatMap(UniverseObject.fromRaw)
    }


}
