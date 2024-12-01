package year_2023.day_013

import scala.io.Source

object Parser {

  def readInput(isSample:Boolean):List[MirrorValley] = {
    if (isSample) {
      """#.##..##.
        |..#.##.#.
        |##......#
        |##......#
        |..#.##.#.
        |..##..##.
        |#.#.##.#.
        |
        |#...##..#
        |#....#..#
        |..##..###
        |#####.##.
        |#####.##.
        |..##..###
        |#....#..#
        |""".stripMargin.split("\n\n").toList.map(_.split("\n").toList).map(parseMirrorValley)
    }
    else Source.fromResource("year_2023/day_013_input.txt").getLines().mkString("\n").split("\n\n").toList.map(_.split("\n").toList).map(parseMirrorValley)
  }

  def parseMirrorValley(lines:List[String]):MirrorValley = {
    MirrorValley(lines.map(_.toList.map(PointInMap.fromRaw)))
  }



}
