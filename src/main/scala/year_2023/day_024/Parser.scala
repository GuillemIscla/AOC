package year_2023.day_024

import scala.io.Source

object Parser {

  def readInput(isSample:Boolean):List[Hailstone] = {
    if (isSample) {
      parseHilestones("""19, 13, 30 @ -2,  1, -2
                       |18, 19, 22 @ -1, -1, -2
                       |20, 25, 34 @ -2, -2, -4
                       |12, 31, 28 @ -1, -2, -1
                       |20, 19, 15 @  1, -5, -3""".stripMargin.split("\n").toList)
    }
    else parseHilestones(Source.fromResource("year_2023/day_024_input.txt").getLines().toList)
  }

  def parseHilestones(lines:List[String]):List[Hailstone] = {
    val hilestoneRegex = "([0-9]+), ([0-9]+), ([0-9]+) @ (( )*(-)*[0-9]+), (( )*(-)*[0-9]+), (( )*(-)*[0-9]+)".r

    lines.map{
      line =>
        val result = hilestoneRegex.findFirstMatchIn(line).getOrElse(throw new Exception(s"Could not parse Hilestone '$line'"))

        Hailstone(
          Coordinate(
            x = result.group(1).toLong,
            y = result.group(2).toLong,
            z = result.group(3).toLong
          ),
          Coordinate(
            x = result.group(4).replace(" ", "").toLong,
            y = result.group(7).replace(" ", "").toLong,
            z = result.group(10).replace(" ", "").toLong
          )
        )
    }
  }


}
