package year_2023.day_017

import year_2023.helper.ListUtils.ListSyntax

import scala.collection.mutable
import scala.util.Try
case class UltraVisitation(coordinate: Coordinate, heatLoss:Int, cardinalRecord:(Cardinal, Int)){
  def move(getHeatLoss:Coordinate => Option[Int])(cardinal: Cardinal):Option[(Cardinal, UltraVisitation)] = {
    val nextCoordinate = cardinal.next(coordinate)
    for {
      additionalHeatLoss <- getHeatLoss(nextCoordinate)
      nextCardinalRecord <- nextCardinalRecord(cardinal)
    } yield
      (cardinal,
        UltraVisitation(
          coordinate = nextCoordinate,
          heatLoss = heatLoss + additionalHeatLoss,
          cardinalRecord = nextCardinalRecord
        )
      )
  }
  def nextCardinalRecord(cardinal:Cardinal):Option[(Cardinal, Int)] = cardinal match {
    case _ if cardinalRecord._1 == cardinal && cardinalRecord._2 == 9 => None
    case _ if cardinalRecord._1 == cardinal => Some((cardinal, cardinalRecord._2 + 1))
    case _ => Some((cardinal, 0))
  }
}

case class UltraPath(visited:List[UltraVisitation], getHeatLoss:Coordinate => Option[Int]){

  val heatLoss:Int = visited.last.heatLoss
  def getNextPaths(markVisitation:UltraVisitation => Boolean):List[UltraPath] = {
    List(North, South, East, West)
      .flatMap(visited.last.move(getHeatLoss))
      .flatMap{
        case (movedCardinal, _) if this.visited.last.cardinalRecord._1 == movedCardinal.reverse =>
          None
        case (movedCardinal, _) if this.visited.size == 1 && this.visited.head.cardinalRecord._1 != movedCardinal =>
          None
        case (movedCardinal, visitation) =>
          val lastMovedCardinal = this.visited.last.cardinalRecord._1
          if(movedCardinal == lastMovedCardinal){
            if (markVisitation(visitation)) {
              Some(UltraPath(visited = visited :+ visitation, getHeatLoss))
            }
            else {
              None
            }
          }
          else if(this.visited.filter(_.cardinalRecord._2 >= 0).takeRight(4).exists(_.cardinalRecord._1 != lastMovedCardinal) || this.visited.filter(_.cardinalRecord._2 >= 0).takeRight(4).size < 4){
            None
          }
          else {
            if (markVisitation(visitation)) {
              Some(UltraPath(visited = visited :+ visitation, getHeatLoss))
            }
            else {
              None
            }
          }
        case (_, visitation) =>
          if(markVisitation(visitation)){
            Some(UltraPath(visited = visited :+ visitation, getHeatLoss))
          }
          else {
            None
          }
      }
  }
}

case class UltraVisitInCell(cardinal: Cardinal, cardinalRepetition:Int, heatLoss:Option[Int])
case class UltraCellVisited(visits:List[UltraVisitInCell] = List(North, South, East, West).flatMap(c => (0 until 10).map(cr => UltraVisitInCell(c,cr,None)))){
  def mark(cardinal: Cardinal, cardinalRepetition:Int, heatLoss:Int):Option[UltraCellVisited] = {
    if(cardinalRepetition > 3) {
      val candidate = UltraVisitInCell(cardinal, cardinalRepetition, Some(heatLoss))
      val justMyCardinal = {
        val filteredByCardinal = visits.filter(_.cardinal == cardinal)
        if (filteredByCardinal.isEmpty) List(candidate)
        else {
          filteredByCardinal.foldLeft((Option.empty[Int], List.empty[UltraVisitInCell])) {
            case ((Some(winner), accList), UltraVisitInCell(_, newCardinalRepetition, maybeWinner)) =>
              (Some(winner), accList :+ UltraVisitInCell(cardinal, newCardinalRepetition, Some(Math.min(maybeWinner.getOrElse(winner), winner))))
            case ((None, accList), vic@UltraVisitInCell(_, newCardinalRepetition, None)) if newCardinalRepetition < candidate.cardinalRepetition =>
              (None, accList :+ vic)
            case ((None, accList), UltraVisitInCell(_, newCardinalRepetition, None)) if newCardinalRepetition >= candidate.cardinalRepetition =>
              (None, accList :+ candidate.copy(cardinalRepetition = newCardinalRepetition))
            case ((None, accList), vic@UltraVisitInCell(_, _, Some(newHeatLoss))) if newHeatLoss <= candidate.heatLoss.get =>
              (Some(newHeatLoss), accList :+ vic)
            case ((None, accList), vic@UltraVisitInCell(_, newCardinalRepetition, Some(newHeatLoss))) if newHeatLoss > candidate.heatLoss.get && newCardinalRepetition < candidate.cardinalRepetition =>
              (None, accList :+ vic)
            case ((None, accList), UltraVisitInCell(_, newCardinalRepetition, Some(newHeatLoss))) if newHeatLoss > candidate.heatLoss.get && newCardinalRepetition >= candidate.cardinalRepetition =>
              (Some(candidate.heatLoss.get), accList :+ candidate.copy(cardinalRepetition = newCardinalRepetition))
          }._2
        }
      }
      val updatedVisits = visits.filter(_.cardinal != cardinal) ++ justMyCardinal
      if (updatedVisits.toSet == visits.toSet) None
      else Some(UltraCellVisited(updatedVisits))
    }
    else {
      val storedVisitIndex = visits.indexWhere{
        storedVisit => storedVisit.cardinalRepetition == cardinalRepetition && storedVisit.cardinal == cardinal
      }
      val storedVisit = visits(storedVisitIndex)
      val newHeatLoss = Math.min(storedVisit.heatLoss.getOrElse(heatLoss), heatLoss)
      val updatedVisits = visits.replace(storedVisitIndex, storedVisit.copy(heatLoss = Some(newHeatLoss)))
      if (updatedVisits.toSet == visits.toSet) None
      else Some(UltraCellVisited(updatedVisits))
    }

  }
}

case class UltraHeatLossMap(map:List[List[Int]]){

  val visitedMap:mutable.ListBuffer[mutable.ListBuffer[UltraCellVisited]] = mutable.ListBuffer.from(map.indices.map{
    x => mutable.ListBuffer.from(map(x).indices.map{
      _ => UltraCellVisited()
    })
  })

  def markAsVisited(visitation:UltraVisitation): Boolean = {
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
    var paths:List[UltraPath] =
      List(
        UltraPath(
          visited = List(UltraVisitation(origin, 0, (East, -1))),
          getHeatLoss = coordinate => Try(map(coordinate.x)(coordinate.y)).toOption
        )
      )
    var i = 0
    while(!paths.exists(_.visited.last.coordinate == destination)){
      paths = iterate(paths)
    }
    paths.find(_.visited.last.coordinate == destination).get.visited.last.heatLoss
  }

  def iterate(paths:List[UltraPath]):List[UltraPath] = {
    val path = paths.head
    val newPaths = path.getNextPaths(markAsVisited)
    (newPaths ++ paths.tail).sortWith{
      case (a, b) =>  Ordering[Int].lt(a.heatLoss, b.heatLoss)
    }
  }



}
