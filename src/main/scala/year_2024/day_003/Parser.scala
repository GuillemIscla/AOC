package year_2024.day_003

import scala.io.Source

object Parser {

  def readInput(isSample:Boolean):List[Instruction] = {
    if(isSample ){
      parseInput("""xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))""")
    }
    else {
      parseInput(Source.fromResource("year_2024/day_003_input.txt").getLines().foldLeft(""){
        case (acc, newLine) => s"$acc\n$newLine"
      })
    }
  }

  def parseInput(input:String):List[Instruction] = {
    val mulPattern = "mul\\(([0-9]+)\\,([0-9]+)\\)".r
    val doPattern = "do\\(\\)".r
    val dontPattern = "don't\\(\\)".r

    val allPattern = s"$mulPattern|$doPattern|$dontPattern".r

    allPattern.findAllIn(input).map{
      case mulPattern(a, b) => Mul(a.toInt,b.toInt)
      case doPattern() => DoOrDont(isDo = true)
      case dontPattern() => DoOrDont(isDo = false)
    }.toList
  }

}
