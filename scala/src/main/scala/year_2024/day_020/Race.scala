package year_2024.day_020

import scala.util.Try

case class Position(x:Int, y:Int)

case class Race(raw:List[List[Char]]) {

  val width:Int = raw.head.size
  val height:Int = raw.size

  def getPosition(position: Position):Option[Char] =
    Try(raw(position.x)(position.y)).toOption


  private def lookFor(char:Char):Position =
    (0 until  width).flatMap{
      i => (0 until height).flatMap {
        j =>
          if(raw(i)(j) == char) Some(Position(i,j))
          else None
      }
    }.head

  val start: Position = lookFor('S')
  val end: Position = lookFor('E')
  val path:List[Position] = {
    var list = List(start)
    while(list.last != end){
      val nextOne =
        List((1, 0), (-1, 0), (0, 1), (0, -1)).map{
          case (offsetX, offsetY) => (list.last.x + offsetX, list.last.y + offsetY)
        }.flatMap {
          case (x, y) =>
            getPosition(Position(x, y)) match {
              case Some(position) if position != '#' && !Try(list(list.size - 2)).toOption.contains(Position(x, y)) =>
                Some(Position(x, y))
              case _ => None
            }
        }
      list = list ++ nextOne
    }
    list
  }

  def savesOnCheat(startCheatPosition: Position):List[(Position, Position, Int)] = {
    List((2, 0), (-2, 0), (0, 2), (0, -2)).map {
      case (offsetX, offsetY) =>
        (Position(startCheatPosition.x + offsetX/2, startCheatPosition.y + offsetY/2),
          Position(startCheatPosition.x + offsetX, startCheatPosition.y + offsetY))
    }.flatMap {
      case (middleCheatPosition, endCheatPosition) =>
        for {
          charMiddle <- getPosition(middleCheatPosition)
          charEnd <- getPosition(endCheatPosition)
          if charMiddle == '#' && charEnd != '#'
          (_, startCheatValue) <- path.zipWithIndex.find(_._1 == startCheatPosition)
          (_, endCheatValue) <- path.zipWithIndex.find(_._1 == endCheatPosition)
          if startCheatValue < endCheatValue -1
        } yield (startCheatPosition, endCheatPosition, endCheatValue - startCheatValue -2)
    }
  }

}
