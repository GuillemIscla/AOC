package year_2023.day_008

import scala.io.Source

object Parser {

  def readInput(isSample:Option[Int], withJoker:Boolean = false):(List[Char], CamelMap) = {
    if (isSample.contains(1)) {
        parseInput("""LLR

        AAA = (BBB, BBB)
        BBB = (AAA, ZZZ)
        ZZZ = (ZZZ, ZZZ)""".stripMargin.split("\n").toList)
    }
    else if(isSample.contains(2)){
      parseInput(
        """LR

        11A = (11B, XXX)
        11B = (XXX, 11Z)
        11Z = (11B, XXX)
        22A = (22B, XXX)
        22B = (22C, 22C)
        22C = (22Z, 22Z)
        22Z = (22B, 22B)
        XXX = (XXX, XXX)""".stripMargin.split("\n").toList)
    }
    else parseInput(Source.fromResource("day_008_input.txt").getLines().toList)
  }

  def parseInput(lines:List[String]):(List[Char], CamelMap) = {
    val step = lines.head.toArray.toList
    val nodes = lines.tail.tail.map(parseNode)
    (step, CamelMap(nodes))
  }

  def parseNode(line:String):CamelMapNode = {
    val nodeRegex = "([A-Z0-9]{3}) = \\(([A-Z0-9]{3})\\, ([A-Z0-9]{3})\\)".r
    nodeRegex.findFirstMatchIn(line) match {
      case Some(regexResult) =>
        CamelMapNode(regexResult.group(1), regexResult.group(2), regexResult.group(3))
      case None =>
        throw new Exception(s"Line '$line' does not follow regex for node")
    }


  }

}
