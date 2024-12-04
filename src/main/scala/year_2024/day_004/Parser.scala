package year_2024.day_004

import scala.io.Source

object Parser {

  def readInput(isSample:Boolean):List[List[Char]] = {
    if(isSample ){
      parseInput("""MMMSXXMASM
         |MSAMXMSMSA
         |AMXSXMAAMM
         |MSAMASMSMX
         |XMASAMXAMM
         |XXAMMXXAMA
         |SMSMSASXSS
         |SAXAMASAAA
         |MAMMMXMMMM
         |MXMXAXMASX""".stripMargin.split("\n").toList)
    }
    else {
      parseInput(Source.fromResource("year_2024/day_004_input.txt").getLines().toList)
    }
  }

  def parseInput(lines:List[String]):List[List[Char]] = lines.map(_.toArray.toList)



}
