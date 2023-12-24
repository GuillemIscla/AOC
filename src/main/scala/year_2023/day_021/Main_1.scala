package year_2023.day_021


object Main_1 extends App {
  var garden = Parser.readInput(isSample = None)

  val start = garden.start

  var visitedToday:List[Coordinate] = List(start)

  val stepsLeftToday = 262

  var i = 0
  while(i < stepsLeftToday){
    visitedToday = visitedToday.flatMap(_.neighboursOpenMap(garden.schema.size, garden.schema.head.size)).filter {
      case Coordinate(x,y, _, _) =>
        garden.schema(x)(y) match {
          case Plot(_) => true
          case Start() => true
          case _ => false

       }
    }.distinct

//    println(s"$i ${visitedToday.size} ${visitedToday.filter{case Coordinate(_, _, map_x, map_y) => Math.abs(map_x) + Math.abs(map_y) <= 4 }.size}")
    i += 1
  }

  println(s"(${i + 1},${visitedToday.groupBy(c => (c.map_x, c.map_y)).view.mapValues(_.size).toMap},")

  println(visitedToday.size)


}
