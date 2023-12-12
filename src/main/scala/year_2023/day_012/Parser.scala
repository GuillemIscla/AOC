package year_2023.day_012

import scala.io.Source

object Parser {

  def readInput(isSample:Boolean):List[HotSpringRecord] = {
    if (isSample) {
      ("???.### 1,1,3\n" ++
      ".??..??...?##. 1,1,3\n" ++
      "?#?#?#?#?#?#?#? 1,3,1,6\n" ++
      "????.#...#... 4,1,1\n" ++
      "????.######..#####. 1,6,5\n" ++
      "?###???????? 3,2,1\n").split("\n").toList.map(parseHotSpringRecord)
    }
    else Source.fromResource("day_011_input.txt").getLines().toList.map(parseHotSpringRecord)
  }

  def parseHotSpringRecord(line:String):HotSpringRecord = {
    val Array(conditionsRaw, damagedGroupsRaw) = line.split(" ")
    HotSpringRecord(
      conditions = conditionsRaw.toList.map(HotSpringCondition.fromRaw),
      damagedGroups = damagedGroupsRaw.split(",").map(_.toInt).toList
    )
  }


}
