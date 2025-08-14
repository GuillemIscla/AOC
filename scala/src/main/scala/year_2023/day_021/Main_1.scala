package year_2023.day_021


object Main_1 extends App {
  var garden = Parser.readInput(isSample = None)

  val start = garden.start

  var visitedToday:List[Coordinate] = List(start)

  val stepsLeftToday = 64

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
    i += 1
  }

  println(visitedToday.size)


}
