package year_2023.day_019

import scala.io.Source

object Parser {

  def readInput(isSample:Boolean):(WorkflowList, List[GearPart]) = {
    if (isSample) {
                  parseInput("""px{a<2006:qkq,m>2090:A,rfg}
                            |pv{a>1716:R,A}
                            |lnx{m>1548:A,A}
                            |rfg{s<537:gd,x>2440:R,A}
                            |qs{s>3448:A,lnx}
                            |qkq{x<1416:A,crn}
                            |crn{x>2662:A,R}
                            |in{s<1351:px,qqz}
                            |qqz{s>2770:qs,m<1801:hdj,R}
                            |gd{a>3333:R,R}
                            |hdj{m>838:A,pv}
                            |
                            |{x=787,m=2655,a=1222,s=2876}
                            |{x=1679,m=44,a=2067,s=496}
                            |{x=2036,m=264,a=79,s=2244}
                            |{x=2461,m=1339,a=466,s=291}
                            |{x=2127,m=1623,a=2188,s=1013}""".stripMargin.split("\n").toList)
    }
    else parseInput(Source.fromResource("year_2023/day_019_input.txt").getLines().toList)
  }

  def parseInput(lines:List[String]):(WorkflowList, List[GearPart]) = {
    val emptyLineIndex = lines.indexWhere(_.isEmpty)
    (WorkflowList(lines.take(emptyLineIndex).map(parseWorkflow)), lines.drop(emptyLineIndex + 1).map(parseGearPart))
  }

  def parseWorkflow(line:String):Workflow = {
    val workflowRegex = "([a-z]+)\\{(([xmas][=<>][0-9]+:[a-zAR]+,)+)([a-zAR]+)\\}".r
    val result = workflowRegex.findFirstMatchIn(line).getOrElse(throw new Exception(s"Could not parse workflow '$line'"))
    val label = result.group(1)
    val wComparisons = result.group(2).split(",").toList.map(parseIfThen)
    val goTo = GoTo.fromRaw(result.group(4))
    Workflow(label, wComparisons, goTo)
  }
  def parseIfThen(line:String):IfThen = {
    val IfThenRegex = "([xmas])([<>])([0-9]+):([a-zAR]+)".r
    val result = IfThenRegex.findFirstMatchIn(line).getOrElse(throw new Exception(s"Could not parse wComparison '$line'"))
    val gearLabelChar = result.group(1).head
    val wComparison = WComparison.fromRaw(result.group(2).head)
    val value = result.group(3).toInt
    val goTo = GoTo.fromRaw(result.group(4))
    IfThen(gearLabelChar, wComparison, value, goTo)
  }

  def parseGearPart(line:String):GearPart = {
    val gearPartRegex = "\\{x=([0-9]+)\\,m=([0-9]+)\\,a=([0-9]+)\\,s=([0-9]+)\\}".r
    val result = gearPartRegex.findFirstMatchIn(line).getOrElse(throw new Exception(s"Could not parse gear '$line'"))
    GearPart(
      x = result.group(1).toInt,
      m = result.group(2).toInt,
      a = result.group(3).toInt,
      s = result.group(4).toInt
    )
  }







}
