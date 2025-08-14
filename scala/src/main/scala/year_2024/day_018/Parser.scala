package year_2024.day_018

import scala.io.Source

object Parser {

  def readInput(isSample:Boolean):MemoryRegion = {
    if(isSample){
      parseInput("""5,4
                   |4,2
                   |4,5
                   |3,0
                   |2,1
                   |6,3
                   |2,4
                   |1,5
                   |0,6
                   |3,3
                   |2,6
                   |5,1
                   |1,2
                   |5,5
                   |2,5
                   |6,5
                   |1,4
                   |0,4
                   |6,4
                   |1,1
                   |6,1
                   |1,0
                   |0,5
                   |1,6
                   |2,0""".stripMargin.split("\n").toList, size = 7)
    }
    else {
      parseInput(Source.fromResource("year_2024/day_018_input.txt").getLines().toList, size = 71)
    }
  }

  def parseInput(lines: List[String], size:Int):MemoryRegion = {
    MemoryRegion(
      raw = List.fill(size)(List.fill(size)('.')),
      fallingBytes = lines.map(line => line.split(',')).collect {
        case Array(x, y) => (y.toInt, x.toInt)
      },
      size = size
    )
  }



}
