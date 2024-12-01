package year_2023.day_017

import scala.io.Source

object Parser {

  def readInput(isSample:Boolean):HeatLossMap = {
    if (isSample) {
      parseInput("""2413432311323
                   |3215453535623
                   |3255245654254
                   |3446585845452
                   |4546657867536
                   |1438598798454
                   |4457876987766
                   |3637877979653
                   |4654967986887
                   |4564679986453
                   |1224686865563
                   |2546548887735
                   |4322674655533""".stripMargin.split("\n").toList)
    }
    else parseInput(Source.fromResource("year_2023/day_017_input.txt").getLines().toList)
  }

  def parseInput(lines:List[String]):HeatLossMap = {
    HeatLossMap(lines.map{
      line => line.toList.map(_.toString.toInt)
    })
  }







}
