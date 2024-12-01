package year_2023.day_015

import scala.io.Source

object Parser {

  def readInput(isSample:Boolean):List[List[Char]] = {
    if (isSample) {
      parseInput("""rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7""")
    }
    else parseInput(Source.fromResource("year_2023/day_015_input.txt").getLines().toList.head)
  }

  def parseInput(line:String):List[List[Char]] = line.split(",").toList.map(_.toList)






}
