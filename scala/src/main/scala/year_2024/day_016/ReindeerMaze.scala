package year_2024.day_016

import scala.annotation.tailrec
import scala.util.Try

case class ReachingCost(position:(Int, Int), cost:Int, lookAt:Cardinal.CardinalValue, isEnd:Boolean, trace:List[(Int, Int)])

object Cardinal extends Enumeration {
  type CardinalValue = Value
  val North, South, East, West = Value
  def newPosition(position:(Int, Int), cardinal: CardinalValue):(Int, Int) = {
    val (x, y) = position
    cardinal match {
      case North => (x - 1, y)
      case South => (x + 1, y)
      case East => (x, y + 1)
      case West => (x, y - 1)
    }
  }
  def all:List[CardinalValue] = List(North, South, East, West)

  def isOpposite(a: CardinalValue, b: CardinalValue): Boolean = {
    (a, b) match {
      case (North, South) => true
      case (South, North) => true
      case (East, West) => true
      case (West, East) => true
      case _ => false
    }
  }

  def turnCost(a: CardinalValue, b: CardinalValue): Int = {
    if (isOpposite(a, b)) 2000
    else if (a != b) 1000
    else 0
  }
}


case class ReindeerMaze(raw:List[List[Char]]){
  import Cardinal._
  val startPosition: (Int, Int) = raw.zipWithIndex.flatMap {
    case (row, i) =>
      row.zipWithIndex.flatMap {
        case (char, j) =>
          if (char == 'S') Some(i, j)
          else None
      }
  }.head

  val endPosition: (Int, Int) = raw.zipWithIndex.flatMap {
    case (row, i) =>
      row.zipWithIndex.flatMap {
        case (char, j) =>
          if (char == 'E') Some(i, j)
          else None
      }
  }.head


  def getPaths(position: (Int, Int)): List[CardinalValue] = {
    val (x, y) = position
    List(
      ((x + 1, y), Cardinal.South),
      ((x - 1, y), Cardinal.North),
      ((x, y + 1), Cardinal.East),
      ((x, y - 1), Cardinal.West)
    ).flatMap {
      case ((x_f, y_f), _) if Try(raw(x_f)(y_f)).isFailure => None
      case ((x_f, y_f), _) if raw(x_f)(y_f) == '#' => None
      case (_, cardinal) => Some(cardinal)
    }
  }


  @tailrec
  final def findNodes(toExplore: List[ReachingCost], explored: List[ReachingCost]): List[ReachingCost] = {
    toExplore match {
      case Nil => explored
      case toExploreHead :: toExploreTail =>
        followPath(toExploreHead.position, toExploreHead.lookAt) match {
          case Some(newPosition) =>
            val foundToExplore = //Go to new position and turn
              Cardinal.all.map {
                cardinal =>
                  val cost =
                    toExploreHead.cost +
                      positionDistance(toExploreHead.position, newPosition) +
                      turnCost(toExploreHead.lookAt, cardinal)
                  val path = buildPath(toExploreHead.position, newPosition)
                  ReachingCost(newPosition, cost, cardinal, isEnd = newPosition == endPosition, trace = newPosition :: (path ++ toExploreHead.trace))
              }
            val newToExplore = foundToExplore.filterNot { //If is explored we don't add it in the newToExplore
              found => explored.exists(inExplored => found.position == inExplored.position && found.lookAt == inExplored.lookAt)
            }.foldLeft(toExploreTail){ //We add//or not//or merge to the other toExplore
              case (acc, newFoundToExplore) => addOrNotOrMerge(newFoundToExplore, acc)
            }
            val newExplored = addOrNotOrMerge(toExploreHead, explored)
            findNodes(newToExplore, newExplored)

          case None =>
            val newExplored = addOrNotOrMerge(toExploreHead, explored)
            findNodes(toExploreTail, newExplored)
        }
    }
  }

  def followPath(position:(Int, Int), lookAt:CardinalValue):Option[(Int, Int)] = {
    val newPositionCalculated = newPosition(position, lookAt)
    val newPaths = getPaths(newPositionCalculated).filterNot(isOpposite(_, lookAt))
    if(raw(newPositionCalculated._1)(newPositionCalculated._2) == '#') None
    else if (newPaths.isEmpty) None
    else if (newPaths == List(lookAt)) followPath(newPositionCalculated, lookAt)
    else Some(newPositionCalculated)
  }

  def positionDistance(a:(Int, Int), b:(Int, Int)):Int =
    Math.abs(a._1 - b._1) + Math.abs(a._2 - b._2)

  def buildPath(a: (Int, Int), b: (Int, Int)): List[(Int, Int)] = {
    import Math._
    if(a._1 == b._1) (min(a._2, b._2) + 1 until max(a._2, b._2)).map{ j => (a._1, j)}.toList
    else (min(a._1, b._1) + 1 until max(a._1, b._1)).map{ i => (i, a._2)}.toList
  }

  def addOrNotOrMerge(newReachingCost: ReachingCost, reachingCostList: List[ReachingCost]):List[ReachingCost] = {
    val isInList = reachingCostList.exists(copyOf => copyOf.position == newReachingCost.position && copyOf.lookAt == newReachingCost.lookAt)
    if(isInList)
      reachingCostList.map{
        case inList@ReachingCost(position, cost, lookAt, _, trace) if position == newReachingCost.position && lookAt == newReachingCost.lookAt =>
          if(newReachingCost.cost < cost) newReachingCost
          else if(newReachingCost.cost > cost) inList
          else inList.copy(trace = (trace ++ newReachingCost.trace).distinct)
        case other => other
      }.sortBy(_.cost)
    else
      (newReachingCost :: reachingCostList).sortBy(_.cost)
  }

}

