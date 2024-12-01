package year_2023.day_022

import scala.io.Source

object Parser {

  def readInput(isSample:Boolean):BricksCascade = {
    if (isSample) {
      parseBricksCascade("""1,0,1~1,2,1
                    |0,0,2~2,0,2
                    |0,2,3~2,2,3
                    |0,0,4~0,2,4
                    |2,0,5~2,2,5
                    |0,1,6~2,1,6
                    |1,1,8~1,1,9""".stripMargin.split("\n").toList)
    }
    else parseBricksCascade(Source.fromResource("year_2023/day_022_input.txt").getLines().toList)
  }

  def parseBricksCascade(lines:List[String]):BricksCascade =
    BricksCascade(lines.map {
      line =>
        val Array(raw1, raw2) = line.split("~")
        val Array(x1,y1,z1) = raw1.split(",")
        val Array(x2,y2,z2) = raw2.split(",")
        Brick.fromCoordinates(Coordinate(x1.toInt,y1.toInt,z1.toInt), Coordinate(x2.toInt,y2.toInt,z2.toInt))
    })



}
