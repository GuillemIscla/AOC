package year_2024.day_013

import scala.io.Source

object Parser {

  def readInput(isSample:Boolean):List[ClawMachine] = {
    if(isSample){
      parseInput("""Button A: X+94, Y+34
                   |Button B: X+22, Y+67
                   |Prize: X=8400, Y=5400
                   |
                   |Button A: X+26, Y+66
                   |Button B: X+67, Y+21
                   |Prize: X=12748, Y=12176
                   |
                   |Button A: X+17, Y+86
                   |Button B: X+84, Y+37
                   |Prize: X=7870, Y=6450
                   |
                   |Button A: X+69, Y+23
                   |Button B: X+27, Y+71
                   |Prize: X=18641, Y=10279""".stripMargin.split("\n").toList)
    }
    else {
      parseInput(Source.fromResource("year_2024/day_013_input.txt").getLines().toList)
    }
  }

  def parseInput(lines: List[String]):List[ClawMachine] = {
    val rawClawMachines:List[(String, String, String)] =
      (lines :+ "").foldLeft((List.empty[(String, String, String)], List.empty[String])){
        case ((accRawClawMachines, accNew), newLine) if accNew.size < 3 =>
          (accRawClawMachines, accNew :+ newLine)
        case ((accRawClawMachines, accNew), _) =>
          val List(buttonARaw, buttonBRaw, prizeRaw) = accNew
          (accRawClawMachines :+ (buttonARaw, buttonBRaw, prizeRaw), List.empty)
      }._1

    val buttonRegex = "Button [A,B]{1}: X\\+([0-9]+), Y\\+([0-9]+)".r
    val prizeRegex = "Prize: X=([0-9]+), Y=([0-9]+)".r
    rawClawMachines.map{
      case (buttonARaw, buttonBRaw, prizeRaw) =>
        val buttonA = buttonRegex.findFirstMatchIn(buttonARaw).map {
          patterMatch => Button(BigInt(patterMatch.group(1).toLong), BigInt(patterMatch.group(2).toLong), 3)
        }.get

        val buttonB = buttonRegex.findFirstMatchIn(buttonBRaw).map {
          patterMatch => Button(BigInt(patterMatch.group(1).toLong), BigInt(patterMatch.group(2).toLong), 1)
        }.get

        val prize = prizeRegex.findFirstMatchIn(prizeRaw).map {
          patterMatch => Prize(BigInt(patterMatch.group(1).toLong), BigInt(patterMatch.group(2).toLong))
        }.get

        ClawMachine(buttonA, buttonB, prize)
    }
  }


}
