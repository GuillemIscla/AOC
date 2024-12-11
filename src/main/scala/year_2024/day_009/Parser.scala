package year_2024.day_009


import scala.io.Source

object Parser {

  def readInput(isSample:Boolean):DiskMap = {
    if(isSample){
      parseInput("""2333133121414131402""")
    }
    else {
      parseInput(Source.fromResource("year_2024/day_009_input.txt").getLines().toList.head)
    }
  }

  def parseInput(line: String):DiskMap = {
    DiskMap(line.toList.map(c => BigInt(c.toString)).foldLeft((List.empty[MemoryItem], BigInt(0), BigInt(0), false)){
      case ((acc, nextId, currentPosition, false), newInt) =>
        (acc  :+ File(nextId, currentPosition, newInt), nextId + 1, currentPosition + newInt, true)
      case ((acc, nextId, currentPosition, true), newInt) =>
        (acc :+ FreeSpace(currentPosition, newInt), nextId, currentPosition + newInt, false)
    }._1)
  }


}
