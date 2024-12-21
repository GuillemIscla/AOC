package year_2024.day_020

import scala.io.Source

object Parser {

  def readInput(isSample:Boolean):Race = {
    if(isSample){
      parseInput("""###############
                   |#...#...#.....#
                   |#.#.#.#.#.###.#
                   |#S#...#.#.#...#
                   |#######.#.#.###
                   |#######.#.#...#
                   |#######.#.###.#
                   |###..E#...#...#
                   |###.#######.###
                   |#...###...#...#
                   |#.#####.#.###.#
                   |#.#...#.#.#...#
                   |#.#.#.#.#.#.###
                   |#...#...#...###
                   |###############
                   |""".stripMargin.split("\n").toList)
    }
    else {
      parseInput(Source.fromResource("year_2024/day_020_input.txt").getLines().toList)
    }
  }

  def parseInput(lines: List[String]):Race = {
    Race(lines.map(_.toList))
  }



}
