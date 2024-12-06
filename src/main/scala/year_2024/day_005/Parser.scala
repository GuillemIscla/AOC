package year_2024.day_005

import scala.io.Source

object Parser {

  def readInput(isSample:Boolean):(List[PageOrderingRule], List[Update]) = {
    if(isSample ){
      parseInput("""47|53
                   |97|13
                   |97|61
                   |97|47
                   |75|29
                   |61|13
                   |75|53
                   |29|13
                   |97|29
                   |53|29
                   |61|53
                   |97|53
                   |61|29
                   |47|13
                   |75|47
                   |97|75
                   |47|61
                   |75|61
                   |47|29
                   |75|13
                   |53|13
                   |
                   |75,47,61,53,29
                   |97,61,53,29,13
                   |75,29,13
                   |75,97,47,61,53
                   |61,13,29
                   |97,13,75,29,47""".stripMargin.split("\n").toList)
    }
    else {
      parseInput(Source.fromResource("year_2024/day_005_input.txt").getLines().toList)
    }
  }

  def parseInput(lines:List[String]):(List[PageOrderingRule], List[Update]) = {
    val separation = lines.indexWhere(_.isEmpty)
    val pageOrderingRules = lines.take(separation).map {
      raw =>
        val split = raw.split("\\|")
        PageOrderingRule(split.head.toInt, split.tail.head.toInt)
    }
    val updates = lines.drop(separation + 1).map{
      raw => Update(raw.split(",").toList.map(_.toInt))
    }
    (pageOrderingRules, updates)
  }


}
