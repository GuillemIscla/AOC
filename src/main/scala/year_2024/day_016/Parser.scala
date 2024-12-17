package year_2024.day_016

import scala.io.Source

object Parser {

  def readInput(isSample:Option[Int]):ReindeerMaze = {
    if(isSample.contains(0)){
      parseInput("""###############
                   |#.......#....E#
                   |#.#.###.#.###.#
                   |#.....#.#...#.#
                   |#.###.#####.#.#
                   |#.#.#.......#.#
                   |#.#.#####.###.#
                   |#...........#.#
                   |###.#.#####.#.#
                   |#...#.....#.#.#
                   |#.#.#.###.#.#.#
                   |#.....#...#.#.#
                   |#.###.#.#.#.#.#
                   |#S..#.....#...#
                   |###############""".stripMargin.split("\n").toList)
    }
    else if (isSample.contains(1)) {
      parseInput("""#################
                   |#...#...#...#..E#
                   |#.#.#.#.#.#.#.#.#
                   |#.#.#.#...#...#.#
                   |#.#.#.#.###.#.#.#
                   |#...#.#.#.....#.#
                   |#.#.#.#.#.#####.#
                   |#.#...#.#.#.....#
                   |#.#.#####.#.###.#
                   |#.#.#.......#...#
                   |#.#.###.#####.###
                   |#.#.#...#.....#.#
                   |#.#.#.#####.###.#
                   |#.#.#.........#.#
                   |#.#.#.#########.#
                   |#S#.............#
                   |#################
                   |""".stripMargin.split("\n").toList)
    }
    else {
      parseInput(Source.fromResource("year_2024/day_016_input.txt").getLines().toList)
    }
  }

  def parseInput(lines: List[String]):ReindeerMaze =
    ReindeerMaze(lines.map(_.toList))



}
