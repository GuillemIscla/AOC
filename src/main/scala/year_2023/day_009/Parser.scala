package year_2023.day_009

import scala.io.Source

object Parser {

  def readInput(isSample:Boolean):List[History] = {
    if (isSample) {
        """0 3 6 9 12 15
        1 3 6 10 15 21
        10 13 16 21 30 45""".split("\n").map(parseHistory).toList
    }
    else Source.fromResource("year_2023/day_009_input.txt").getLines().map(parseHistory).toList
  }

  def parseHistory(line:String):History =
    History(line.split(" ").flatMap(_.toIntOption).toList)


}
