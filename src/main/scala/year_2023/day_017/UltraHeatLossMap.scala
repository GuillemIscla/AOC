package year_2023.day_017

import scala.collection.mutable
import scala.util.Try

case class Coordinate(x:Int, y:Int)

trait Cardinal{
  def next(coordinate: Coordinate):Coordinate
  def reverse:Cardinal
}

case object North extends Cardinal {
  override def next(coordinate: Coordinate): Coordinate = Coordinate(coordinate.x - 1, coordinate.y)
  def reverse:Cardinal = South
}
case object South extends Cardinal{
  override def next(coordinate: Coordinate): Coordinate = Coordinate(coordinate.x + 1, coordinate.y)

  def reverse:Cardinal = North
}
case object East extends Cardinal{
  override def next(coordinate: Coordinate): Coordinate = Coordinate(coordinate.x, coordinate.y + 1)

  def reverse:Cardinal = West
}
case object West extends Cardinal{
  override def next(coordinate: Coordinate): Coordinate = Coordinate(coordinate.x, coordinate.y - 1)

  def reverse:Cardinal = East
}

case class Visitation(coordinate: Coordinate, heatLoss:Int, cardinalRecord:(Cardinal, Int)){
  def move(getHeatLoss:Coordinate => Option[Int])(cardinal: Cardinal):Option[(Cardinal, Visitation)] = {
    val nextCoordinate = cardinal.next(coordinate)
    for {
      additionalHeatLoss <- getHeatLoss(nextCoordinate)
      nextCardinalRecord <- nextCardinalRecord(cardinal)
    } yield
      (cardinal,
        Visitation(
          coordinate = nextCoordinate,
          heatLoss = heatLoss + additionalHeatLoss,
          cardinalRecord = nextCardinalRecord
        )
      )
  }
  def nextCardinalRecord(cardinal:Cardinal):Option[(Cardinal, Int)] = cardinal match {
    case _ if cardinalRecord._1 == cardinal && cardinalRecord._2 == 2 => None
    case _ if cardinalRecord._1 == cardinal => Some((cardinal, cardinalRecord._2 + 1))
    case _ => Some((cardinal, 0))
  }
}

case class Path(visited:List[Visitation], getHeatLoss:Coordinate => Option[Int]){

  val heatLoss:Int = visited.last.heatLoss
  def getNextPaths(markVisitation:Visitation => Boolean):List[Path] = {
    List(North, South, East, West)
      .flatMap(visited.last.move(getHeatLoss))
      .flatMap{
        case (movedCardinal, _) if this.visited.last.cardinalRecord._1 == movedCardinal.reverse =>
          None
        case (_, visitation) =>
          if(markVisitation(visitation)){
            Some(Path(visited = visited :+ visitation, getHeatLoss))
          }
          else {
            None
          }
      }
  }
}

case class VisitInCell(cardinal: Cardinal, cardinalRepetition:Int, heatLoss:Option[Int])
case class CellVisited(visits:List[VisitInCell] = List(North, South, East, West).flatMap(c => (0 until 3).map(cr => VisitInCell(c,cr,None)))){
  def mark(cardinal: Cardinal, cardinalRepetition:Int, heatLoss:Int):Option[CellVisited] = {
    val candidate = VisitInCell(cardinal, cardinalRepetition, Some(heatLoss))
    val justMyCardinal = {
      val filteredByCardinal = visits.filter(_.cardinal == cardinal)
      if(filteredByCardinal.isEmpty) List(candidate)
      else{
        filteredByCardinal.foldLeft((Option.empty[Int], List.empty[VisitInCell])) {
          case ((Some(winner), accList), VisitInCell(_, newCardinalRepetition, maybeWinner)) =>
            (Some(winner), accList :+ VisitInCell(cardinal, newCardinalRepetition, Some(Math.min(maybeWinner.getOrElse(winner), winner))))
          case ((None, accList), vic@VisitInCell(_, newCardinalRepetition, None)) if newCardinalRepetition < candidate.cardinalRepetition =>
            (None, accList :+ vic)
          case ((None, accList), VisitInCell(_, newCardinalRepetition, None)) if newCardinalRepetition >= candidate.cardinalRepetition =>
            (None, accList :+ candidate.copy(cardinalRepetition = newCardinalRepetition))
          case ((None, accList), vic@VisitInCell(_, _, Some(newHeatLoss))) if newHeatLoss <= candidate.heatLoss.get =>
            (Some(newHeatLoss), accList :+ vic)
          case ((None, accList), vic@VisitInCell(_, newCardinalRepetition, Some(newHeatLoss))) if newHeatLoss > candidate.heatLoss.get && newCardinalRepetition < candidate.cardinalRepetition =>
            (None, accList :+ vic)
          case ((None, accList), VisitInCell(_, newCardinalRepetition, Some(newHeatLoss))) if newHeatLoss > candidate.heatLoss.get && newCardinalRepetition >= candidate.cardinalRepetition =>
            (Some(candidate.heatLoss.get), accList :+ candidate.copy(cardinalRepetition = newCardinalRepetition))
        }._2
      }
    }
    val updatedVisits = visits.filter(_.cardinal != cardinal) ++ justMyCardinal
    if(updatedVisits.toSet == visits.toSet) None
    else Some(CellVisited(updatedVisits))
  }
}

case class HeatLossMap(map:List[List[Int]]){

  val visitedMap:mutable.ListBuffer[mutable.ListBuffer[CellVisited]] = mutable.ListBuffer.from(map.indices.map{
    x => mutable.ListBuffer.from(map(x).indices.map{
      _ => CellVisited()
    })
  })

  def markAsVisited(visitation:Visitation): Boolean = {
    val (x,y,cardinal,cardinalRepetition,heatLoss) = (visitation.coordinate.x, visitation.coordinate.y, visitation.cardinalRecord._1, visitation.cardinalRecord._2, visitation.heatLoss)
    visitedMap(x)(y).mark(cardinal, cardinalRepetition, heatLoss) match {
      case Some(newVisitedCell) =>
        visitedMap(x)(y) = newVisitedCell
        true
      case None =>
        false
    }
  }

  override def toString: String = map.map(_.mkString).mkString("\n")

  val origin:Coordinate = Coordinate(0, 0)
  val destination:Coordinate = Coordinate(map.size -1 , map.head.size -1)

  def getMinPath:Int = {
    var paths:List[Path] =
      List(
        Path(
          visited = List(Visitation(origin, 0, (East, -1))),
          getHeatLoss = coordinate => Try(map(coordinate.x)(coordinate.y)).toOption
        )
      )
    var i = 0
    while(!paths.exists(_.visited.last.coordinate == destination)){
      i += 1
      if(i % 10000 == 0) {
        println(paths.head)
      }
      paths = iterate(paths)
    }
    println(paths.find(_.visited.last.coordinate == destination))
    paths.find(_.visited.last.coordinate == destination).get.visited.last.heatLoss
  }

  def iterate(paths:List[Path]):List[Path] = {
    val path = paths.head
    val newPaths = path.getNextPaths(markAsVisited)
    (newPaths ++ paths.tail).sortWith{
      case (a, b) =>  Ordering[Int].lt(a.heatLoss, b.heatLoss)
    }
  }



}
