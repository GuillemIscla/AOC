package year_2023.day_014

import scala.io.Source

object Parser {

  def readInput(isSample:Boolean):RockField = {
    if (isSample) {
      parseRockField("""O....#....
        |O.OO#....#
        |.....##...
        |OO.#O....O
        |.O.....O#.
        |O.#..O.#.#
        |..O..#O..O
        |.......O..
        |#....###..
        |#OO..#....""".stripMargin.split("\n").toList)
    }
    else parseRockField(Source.fromResource("year_2023/day_014_input.txt").getLines().toList)
  }

  def parseRockField(lines:List[String]):RockField = {
    RockField(lines.map(_.toList.map(RockItem.fromRaw)))
  }






}
