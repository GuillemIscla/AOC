package year_2024.day_018

import scala.annotation.tailrec

object Main2 extends App {

  val isSample = false

  val memoryRegion = Parser.readInput(isSample)

  val initial = if(isSample) 12 else 1024

  val firstBlockingByte = findMinimum(initial, memoryRegion.fallingBytes.size)

  println(s"${firstBlockingByte._2},${firstBlockingByte._1}")

  @tailrec
  def findMinimum(lower:Int, upper:Int):(Int, Int) = {
    if(lower + 1 < upper){
      val middle = (upper + lower) / 2
      val memoryRegionAfterFall = memoryRegion.fallBytes(middle)
      val reachingEnd = {
        memoryRegionAfterFall.findNodes(
          toExplore = List(ReachingCost(position = (0, 0), isEnd = false, trace = List((0, 0)))),
          explored = List.empty
        ).exists(_.isEnd)
      }
      if(reachingEnd) findMinimum(middle, upper)
      else findMinimum(lower, middle)
    }
    else {
      memoryRegion.fallingBytes(lower)
    }
  }


}
