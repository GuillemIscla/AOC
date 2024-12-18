package year_2024.day_018


import scala.annotation.tailrec

case class ReachingCost(position:(Int, Int), isEnd:Boolean, trace:List[(Int, Int)])

case class MemoryRegion(raw:List[List[Char]], fallingBytes:List[(Int, Int)], size:Int) {
  private val endPosition:(Int, Int) = (size -1, size -1)

  def fallBytes(count:Int):MemoryRegion = {
    MemoryRegion(
      raw = raw.zipWithIndex.map{
        case (row, i) =>
          row.zipWithIndex.map{
            case (char, j) =>
              if(fallingBytes.take(count).contains((i, j))) '#'
              else char
          }
      },
      fallingBytes = fallingBytes.drop(count),
      size = size
    )
  }


  @tailrec
  final def findNodes(toExplore: List[ReachingCost], explored: List[ReachingCost]): List[ReachingCost] = {
    toExplore match {
      case Nil => explored
      case toExploreHead :: toExploreTail =>
        val foundToExplore = findPositions(toExploreHead.position).map{
          newPosition =>
            ReachingCost(newPosition, isEnd = newPosition == endPosition, trace = newPosition :: toExploreHead.trace)
        }
        val newToExplore = foundToExplore.filterNot { //If is explored we don't add it in the newToExplore
          found => explored.exists(inExplored => found.position == inExplored.position)
        }.foldLeft(toExploreTail) { //We add//or not//or merge to the other toExplore
          case (acc, newFoundToExplore) => addOrNotOrMerge(newFoundToExplore, acc)
        }
        val newExplored = addOrNotOrMerge(toExploreHead, explored)
        findNodes(newToExplore, newExplored)
    }
  }

  def printMap(trace: List[(Int, Int)]): Unit = {
    raw.zipWithIndex.foreach {
      case (row, i) =>
        row.zipWithIndex.foreach {
          case (char, j) =>
            if (trace.contains((i, j))) print('O')
            else print(char)
        }
        println()
    }
  }

  private def findPositions(initial: (Int, Int)):List[(Int, Int)] = {
    List((0, 1), (0, -1), (1, 0), (-1, 0)).map{
      case (offset_x , offset_y) => (initial._1 + offset_x, initial._2 + offset_y)
    }.filter{
      case (x, y) =>
        x >= 0 && x < size && y >= 0 && y < size && raw(x)(y) == '.'
    }
  }

  private def addOrNotOrMerge(newReachingCost: ReachingCost, reachingCostList: List[ReachingCost]): List[ReachingCost] = {
    val isInList = reachingCostList.exists(copyOf => copyOf.position == newReachingCost.position)
    if (isInList)
      reachingCostList.map {
        case inList@ReachingCost(position, _, trace) if position == newReachingCost.position =>
          if (newReachingCost.trace.size < trace.size) newReachingCost
          else inList
        case other => other
      }.sortBy(_.trace.size)
    else
      (newReachingCost :: reachingCostList).sortBy(_.trace.size)
  }
}
