package year_2024.day_006

import scala.collection.mutable
import scala.io.Source

object Parser {

  def readInput(isSample:Boolean):Map = {
    if(isSample ){
      parseInput("""....#.....
                   |.........#
                   |..........
                   |..#.......
                   |.......#..
                   |..........
                   |.#..^.....
                   |........#.
                   |#.........
                   |......#...""".stripMargin.split("\n").toList)
    }
    else {
      parseInput(Source.fromResource("year_2024/day_006_input.txt").getLines().toList)
    }
  }

  def parseInput(lines: List[String]):Map = {
    Map(mutable.ListBuffer.from(lines.map{
      line =>
        mutable.ListBuffer.from(line.toCharArray.toList)

    }))
  }



}
