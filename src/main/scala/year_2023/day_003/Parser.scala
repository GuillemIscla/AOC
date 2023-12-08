package year_2023.day_003

import scala.io.Source

object Parser {

  def readInput(isSample:Boolean): EngineSchematic = {
    if(isSample){
      new EngineSchematic("""467..114..
        |...*......
        |..35..633.
        |......#...
        |617*......
        |.....+.58.
        |..592.....
        |......755.
        |...$.*....
        |.664.598..""".split("\\|").toList.map(_.toArray.toList))
    }
    else new EngineSchematic(Source.fromResource("day3_input.txt").getLines().map(_.toArray.toList).toArray.toList)
  }
}
