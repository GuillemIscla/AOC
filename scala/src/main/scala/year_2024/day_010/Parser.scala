package year_2024.day_010

import scala.io.Source

object Parser {

  def readInput(isSample:Option[Int]):TopographicMap = {
    if(isSample.contains(0)){
      parseInput("""0123
                   |1234
                   |8765
                   |9876""".stripMargin.split("\n").toList)
    } else if(isSample.contains(1)){
      parseInput("""89010123
                   |78121874
                   |87430965
                   |96549874
                   |45678903
                   |32019012
                   |01329801
                   |10456732""".stripMargin.split("\n").toList)
    }
    else {
      parseInput(Source.fromResource("year_2024/day_010_input.txt").getLines().toList)
    }
  }

  def parseInput(lines: List[String]):TopographicMap = {
    TopographicMap(lines.map(_.toArray.map(_.toString.toInt).toList))
  }


}
