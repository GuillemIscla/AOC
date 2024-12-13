package year_2024.day_012

import scala.annotation.tailrec

case class Position(x:Int, y:Int, char:Char)

trait Fence {
  def toSide:Side
}

case class VerticalFence(x:Int, y:(Option[Int], Option[Int]), insideIsUp:Boolean) extends Fence{
  def toSide:Side = VerticalSide(x, x, y, insideIsUp)
}
case class HorizontalFence(x:(Option[Int], Option[Int]), y:Int, insideIsLeft:Boolean) extends Fence {
  override def toSide: Side = HorizontalSide(x, y, y, insideIsLeft)
}

case class CandidateToMerge(isLeft:Boolean, fence: Fence)
trait Side {
  def candidatesToMerge(width:Int, height:Int):List[CandidateToMerge]
  def addCandidateToMerge(isLeft:Boolean):Side
}

case class VerticalSide(xMin:Int, xMax:Int, y:(Option[Int], Option[Int]), insideIsUp:Boolean) extends Side {
  override def candidatesToMerge(width:Int, height:Int): List[CandidateToMerge] =
    List(
      CandidateToMerge(isLeft = true, VerticalFence(xMin - 1, y, insideIsUp)),
      CandidateToMerge(isLeft = false, VerticalFence(xMax + 1, y, insideIsUp))
    )

  override def addCandidateToMerge(isLeft: Boolean): Side = {
    if(isLeft) VerticalSide(xMin - 1, xMax, y, insideIsUp)
    else VerticalSide(xMin, xMax + 1, y, insideIsUp)
  }
}
case class HorizontalSide(x:(Option[Int], Option[Int]), yMin:Int, yMax:Int, insideIsLeft:Boolean) extends Side {
  override def candidatesToMerge(width:Int, height:Int): List[CandidateToMerge] =
    List(
      CandidateToMerge(isLeft = true, HorizontalFence(x, yMin - 1, insideIsLeft)),
      CandidateToMerge(isLeft = false, HorizontalFence(x, yMax + 1, insideIsLeft))
    )

  override def addCandidateToMerge(isLeft: Boolean): Side =
    if (isLeft) HorizontalSide(x, yMin - 1, yMax, insideIsLeft)
    else HorizontalSide(x, yMin, yMax + 1, insideIsLeft)
}



case class Garden(raw:List[List[Char]]){
  val width:Int = raw.head.size
  val height:Int = raw.size

  def getAllAreas:List[Set[Position]] = {
    var remainingPositions = raw.zipWithIndex.flatMap {
      case (row, i) => row.zipWithIndex.map{
        case (char, j) => Position(i, j, char)
      }
    }.toSet
    var areas = List.empty[Set[Position]]
    while(remainingPositions.nonEmpty){
      val initial = remainingPositions.head
      val area = expandArea(initial)
      areas = areas :+ area
      remainingPositions = remainingPositions -- area
    }
    areas
  }

  def getAllFences(area:Set[Position]):Set[Fence] = area.flatMap{
    case Position(x, y, _) =>
      Set(
        VerticalFence(x, (Option.when(y - 1 >= 0)(y - 1), Some(y)), insideIsUp = false),
        VerticalFence(x, (Some(y), Option.when(y + 1 < height)(y + 1)), insideIsUp = true),
        HorizontalFence((Option.when(x - 1 >= 0)(x - 1), Some(x)), y, insideIsLeft = false),
        HorizontalFence((Some(x), Option.when(x + 1 < width)(x + 1)), y, insideIsLeft = true)
      )
  }

  private def expandArea(initial:Position): Set[Position] =
    expandAreaInternal(visited = Set.empty, toVisit = List(initial), char = initial.char)
      .filter(_.char == initial.char)


  @tailrec
  private def expandAreaInternal(visited:Set[Position], toVisit:List[Position], char:Char):Set[Position] = {
    toVisit match{
      case head :: tail if head.char == char =>
        val newVisited = visited + head
        val neighbours = List(
          (head.x - 1, head.y),
          (head.x + 1, head.y),
          (head.x, head.y - 1 ),
          (head.x, head.y + 1)
        ).filter{
          case (x, y) =>
            x >= 0 && x < width && y >= 0 && y < height &&
              !visited.exists(p => p.x == x && p.y == y) &&
              !toVisit.exists(p => p.x == x && p.y == y)
        }.map{
          case (x, y) => Position(x,y,raw(x)(y))
        }
        expandAreaInternal(newVisited, tail ++ neighbours, char)

      case head :: tail =>
        expandAreaInternal(visited + head, tail, char)
      case Nil =>
        visited

    }
  }

  def fencesToSides(fences:Set[Fence]):Set[Side] = {
    var remainingFences = fences
    var sides = Set.empty[Side]

    while(remainingFences.nonEmpty){
      var side = remainingFences.head.toSide
      remainingFences = remainingFences - remainingFences.head
      var sideCandidates = side.candidatesToMerge(width, height).filter(sc => remainingFences(sc.fence))
      while (sideCandidates.map(_.fence).toSet.intersect(remainingFences).nonEmpty) {
        side = sideCandidates.foldLeft(side) {
          case (acc, CandidateToMerge(isLeft, _)) => acc.addCandidateToMerge(isLeft)
        }
        remainingFences = remainingFences -- sideCandidates.map(_.fence)
        sideCandidates = side.candidatesToMerge(width, height).filter(sc => remainingFences(sc.fence))
      }
      sides = sides + side
    }
    sides
  }

}
