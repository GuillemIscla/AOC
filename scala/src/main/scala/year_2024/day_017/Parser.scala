package year_2024.day_017

import scala.io.Source

object Parser {

  def readInput(isSample:Option[Int]):ThreeBitProgram = {
    if(isSample.contains(0)){
      parseInput("""Register A: 729
                   |Register B: 0
                   |Register C: 0
                   |
                   |Program: 0,1,5,4,3,0""".stripMargin.split("\n").toList)
    }
    else if (isSample.contains(1)) {
      parseInput(
        """Register A: 117440
          |Register B: 0
          |Register C: 0
          |
          |Program: 0,3,5,4,3,0""".stripMargin.split("\n").toList)
    }
    else {
      parseInput(Source.fromResource("year_2024/day_017_input.txt").getLines().toList)
    }
  }

  def parseInput(lines: List[String]):ThreeBitProgram = {
    val registers = Registers(
      registerA = lines.head.split(':')(1).trim.toLong,
      registerB = lines(1).split(':')(1).trim.toLong,
      registerC = lines(2).split(':')(1).trim.toLong,
    )
    val instructions = lines(4).split(':')(1).split(',').map(_.trim.toInt).toList
    ThreeBitProgram(registers, instructions)
  }



}
