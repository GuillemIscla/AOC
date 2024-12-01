package year_2023.day_018

import scala.io.Source

object Parser {

  def readInput(isSample:Option[Int]):DigPlan = {
    if (isSample.contains(0)) {
                  DigPlan("""R 6 (#70c710)
                   |D 5 (#0dc571)
                   |L 2 (#5713f0)
                   |D 2 (#d2c081)
                   |R 2 (#59c680)
                   |D 2 (#411b91)
                   |L 5 (#8ceee2)
                   |U 2 (#caa173)
                   |L 1 (#1b58a2)
                   |U 2 (#caa171)
                   |R 2 (#7807d2)
                   |U 3 (#a77fa3)
                   |L 2 (#015232)
                   |U 2 (#7a21e3)""".stripMargin.split("\n").toList.map(parseDigPlanStep))
    }
    else if(isSample.contains(1)) {
      DigPlan(
        """R 6 (#70c710)
          |D 5 (#0dc571)
          |L 2 (#5713f0)""".stripMargin.split("\n").toList.map(parseDigPlanStep))
    }
    else DigPlan(Source.fromResource("year_2023/day_018_input.txt").getLines().toList.map(parseDigPlanStep))
  }

  def parseDigPlanStep(line:String):DigPlanStep = {
    val digPlanRegex = "([A-Z]) ([0-9]+) \\((#[a-z,0-9]+)\\)".r

    val result = digPlanRegex.findFirstMatchIn(line).getOrElse(throw new Exception(s"Could not parse line '$line'"))

    DigPlanStep(
      direction = Direction.fromRaw(result.group(1).head),
      deep = result.group(2).toIntOption.getOrElse(throw new Exception(s"Could not parse '${result.group(2)}' into a number")),
      hexColor = result.group(3)
    )

  }







}
