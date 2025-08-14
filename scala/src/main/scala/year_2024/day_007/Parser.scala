package year_2024.day_007

import scala.collection.mutable
import scala.io.Source

object Parser {

  def readInput(isSample:Boolean):List[Equation] = {
    if(isSample ){
      parseInput("""190: 10 19
                   |3267: 81 40 27
                   |83: 17 5
                   |156: 15 6
                   |7290: 6 8 6 15
                   |161011: 16 10 13
                   |192: 17 8 14
                   |21037: 9 7 18 13
                   |292: 11 6 16 20""".stripMargin.split("\n").toList)
    }
    else {
      parseInput(Source.fromResource("year_2024/day_007_input.txt").getLines().toList)
    }
  }

  def parseInput(lines: List[String]):List[Equation] = {
    lines.flatMap {
      line => line.split(":") match {
        case Array(resultRaw, operandsRaw) =>
          Some(Equation(resultRaw.toLong, operandsRaw.split(" ").flatMap(_.toLongOption).toList))
        case _ => None
      }
    }
  }



}
