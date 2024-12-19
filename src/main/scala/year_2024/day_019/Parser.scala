package year_2024.day_019

import scala.io.Source

object Parser {

  def readInput(isSample:Boolean):(List[Towel], List[Design]) = {
    if(isSample){
      parseInput("""r, wr, b, g, bwu, rb, gb, br
                   |
                   |brwrr
                   |bggr
                   |gbbr
                   |rrbgbr
                   |ubwu
                   |bwurrg
                   |brgr
                   |bbrgwb""".stripMargin.split("\n").toList)
    }
    else {
      parseInput(Source.fromResource("year_2024/day_019_input.txt").getLines().toList)
    }
  }

  def parseInput(lines: List[String]):(List[Towel], List[Design]) = {
    val towels = lines.head.split(", ").map(_.toList).map(Towel.apply).toList
    val designs = lines.drop(2).map(_.toList).map(Design.apply).toList
    (towels, designs)
  }



}
