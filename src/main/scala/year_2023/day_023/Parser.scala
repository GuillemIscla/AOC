package year_2023.day_023

import scala.io.Source

object Parser {

  def readInput(isSample:Boolean):HikingMap = {
    if (isSample) {
      parseHikingMap("""#.#####################
                       |#.......#########...###
                       |#######.#########.#.###
                       |###.....#.>.>.###.#.###
                       |###v#####.#v#.###.#.###
                       |###.>...#.#.#.....#...#
                       |###v###.#.#.#########.#
                       |###...#.#.#.......#...#
                       |#####.#.#.#######.#.###
                       |#.....#.#.#.......#...#
                       |#.#####.#.#.#########v#
                       |#.#...#...#...###...>.#
                       |#.#.#v#######v###.###v#
                       |#...#.>.#...>.>.#.###.#
                       |#####v#.#.###v#.#.###.#
                       |#.....#...#...#.#.#...#
                       |#.#########.###.#.#.###
                       |#...###...#...#...#.###
                       |###.###.#.###v#####v###
                       |#...#...#.#.>.>.#.>.###
                       |#.###.###.#.###.#.#v###
                       |#.....###...###...#...#
                       |#####################.#""".stripMargin.split("\n").toList)
    }
    else parseHikingMap(Source.fromResource("year_2023/day_023_input.txt").getLines().toList)
  }

  def parseHikingMap(lines:List[String]):HikingMap =
    HikingMap(lines.zipWithIndex.map{
      case (line,x) => line.toList.zipWithIndex.map{
        case (raw,y) => HikingTile.fromRaw(raw, Coordinate(x,y))
      }
    })



}
