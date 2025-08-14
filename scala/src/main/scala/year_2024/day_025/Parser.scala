package year_2024.day_025

import scala.io.Source

object Parser {

  def readInput(isSample:Boolean):List[Schematic]  = {
    if(isSample){
      parseInput("""#####
                   |.####
                   |.####
                   |.####
                   |.#.#.
                   |.#...
                   |.....
                   |
                   |#####
                   |##.##
                   |.#.##
                   |...##
                   |...#.
                   |...#.
                   |.....
                   |
                   |.....
                   |#....
                   |#....
                   |#...#
                   |#.#.#
                   |#.###
                   |#####
                   |
                   |.....
                   |.....
                   |#.#..
                   |###..
                   |###.#
                   |###.#
                   |#####
                   |
                   |.....
                   |.....
                   |.....
                   |#....
                   |#.#..
                   |#.#.#
                   |#####
                   |
                   |""".stripMargin.split("\n").toList)
    }
    else {
      parseInput(Source.fromResource("year_2024/day_025_input.txt").getLines().toList)
    }
  }

  def parseInput(lines: List[String]):List[Schematic] = {
    val (schematics, _) = (lines :+ "").zipWithIndex.foldLeft((List.empty[Schematic], List.empty[List[Char]])){
      case ((schematicsAcc, rawAcc), (_, index)) if (index + 1) % 8 == 0 =>
        (schematicsAcc :+ Schematic(rawAcc), List.empty)
      case ((schematicsAcc, rawAcc), (newLine, _)) =>
        (schematicsAcc, rawAcc :+ newLine.toList)
    }
    schematics
  }




}
