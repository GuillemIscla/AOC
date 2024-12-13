package year_2024.day_012

import scala.io.Source

object Parser {

  def readInput(isSample:Option[Int]):Garden = {
    if(isSample.contains(0)){
      parseInput("""AAAA
                   |BBCD
                   |BBCC
                   |EEEC""".stripMargin.split("\n").toList)
    }
    else if(isSample.contains(1)){
      parseInput(
        """OOOOO
          |OXOXO
          |OOOOO
          |OXOXO
          |OOOOO""".stripMargin.split("\n").toList)
    }
    else if (isSample.contains(2)) {
      parseInput(
        """RRRRIICCFF
          |RRRRIICCCF
          |VVRRRCCFFF
          |VVRCCCJFFF
          |VVVVCJJCFE
          |VVIVCCJJEE
          |VVIIICJJEE
          |MIIIIIJJEE
          |MIIISIJEEE
          |MMMISSJEEE
          |""".stripMargin.split("\n").toList)
    }
    else if (isSample.contains(3)) {
      parseInput(
        """EEEEE
          |EXXXX
          |EEEEE
          |EXXXX
          |EEEEE""".stripMargin.split("\n").toList)
    }
    else if (isSample.contains(4)) {
      parseInput(
        """AAAAAA
          |AAABBA
          |AAABBA
          |ABBAAA
          |ABBAAA
          |AAAAAA""".stripMargin.split("\n").toList)
    }
    else {
      parseInput(Source.fromResource("year_2024/day_012_input.txt").getLines().toList)
    }
  }

  def parseInput(lines: List[String]):Garden =
    Garden(lines.map(line => line.toList))



}
