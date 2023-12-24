package year_2023.day_021

object Iterator {

  def iterate(start:Coordinate, garden: Garden, stepsLeftToday:Long):Long = {
    var toVisitToday: Set[Coordinate] = Set(start)
    var visitedYesterday: Set[Coordinate] = Set()
    var (visitOnOdds, visitOnEvens) = (1, 0)

    var i = 0
    while (i < stepsLeftToday) {
      val neighbours = toVisitToday.flatMap(_.neighboursOpenMap(garden.schema.size, garden.schema.head.size)).filter {
        case coord@Coordinate(x, y, _, _) =>
          garden.schema(x)(y) match {
            case Rock() => false
            case _ => !visitedYesterday.contains(coord)
          }
      }
      if (i % 2 == 0) visitOnEvens += neighbours.size else visitOnOdds += neighbours.size
      visitedYesterday = toVisitToday
      toVisitToday = neighbours
      i += 1
    }

    val result = if (i % 2 == 0) visitOnOdds else visitOnEvens
    result
  }

  def iterateClosed(start: Coordinate, garden: Garden, stepsLeftToday: Long): Long = {
    var toVisitToday: Set[Coordinate] = Set(start)
    var visitedYesterday: Set[Coordinate] = Set()
    var (visitOnOdds, visitOnEvens) = (1, 0)

    var i = 0
    while (i < stepsLeftToday) {
      val neighbours = toVisitToday.flatMap(_.neighboursClosedMap(garden.schema.size, garden.schema.head.size)).filter {
        case coord@Coordinate(x, y, _, _) =>
          garden.schema(x)(y) match {
            case Rock() => false
            case _ => !visitedYesterday.contains(coord)
          }
      }
      if (i % 2 == 0) visitOnEvens += neighbours.size else visitOnOdds += neighbours.size
      visitedYesterday = toVisitToday
      toVisitToday = neighbours
      i += 1
    }

    val result = if (i % 2 == 0) visitOnOdds else visitOnEvens
    result
  }
  def getStats(start: Coordinate, garden: Garden, stepsLeftToday: Long): StatComputer = {
    var toVisitToday: Set[Coordinate] = Set(start)
    var visitedYesterday: Set[Coordinate] = Set()
    var (visitOnOdds, visitOnEvens) = (1, 0)


    var stats:Map[(Int, Int), Stat] = Map.empty
    var tracking:List[(Int, Int)] = List.empty

    var i = 0
    while (i < stepsLeftToday) {
      val newForTrack = toVisitToday.collect {
        case Coordinate(x, y, map_x, map_y) if !tracking.contains((map_x, map_y)) =>
          Stat((map_x, map_y), i, -1, (x,y))
      }
      val oldTracked = tracking.filterNot{
        case (map_x, map_y) => toVisitToday.exists(coord => coord.map_x == map_x && coord.map_y == map_y)
      }.flatMap(stats.get)
      stats =
        stats ++
          newForTrack.map{stat => stat.mapCoordinate -> stat } ++
          oldTracked.map{ stat => stat.mapCoordinate -> stat.copy(finishIteration = i) }

      tracking =
        tracking
          .filterNot{ trackedMap => oldTracked.exists(stat => stat.mapCoordinate == trackedMap)} ++
          newForTrack.map(_.mapCoordinate)
      val neighbours = toVisitToday.flatMap(_.neighboursOpenMap(garden.schema.size, garden.schema.head.size)).filter {
        case coord@Coordinate(x, y, _, _) =>
          garden.schema(x)(y) match {
            case Rock() => false
            case _ => !visitedYesterday.contains(coord)
          }
      }
      if (i % 2 == 0) visitOnEvens += neighbours.size else visitOnOdds += neighbours.size
      visitedYesterday = toVisitToday
      toVisitToday = neighbours
      i += 1
    }

    StatComputer(garden, stats.values.toList)
  }

}
