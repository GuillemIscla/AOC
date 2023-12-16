package year_2023.day_016

import scala.io.Source

object Parser {

  def readInput(isSample:Boolean):Contraption = {
    if (isSample) {
      parseContraption(""".|...\....
                   ||.-.\.....
                   |.....|-...
                   |........|.
                   |..........
                   |.........\
                   |..../.\\..
                   |.-.-/..|..
                   |.|....-|.\
                   |..//.|....""".stripMargin.split("\n").toList)
    }
    else parseContraption(Source.fromResource("day_016_input.txt").getLines().toList)
  }

  def parseContraption(lines:List[String]):Contraption =
    Contraption(lines.map(_.toList.map(Tile.fromRaw)))



}
