package year_2023.day_021

case class Coordinate(x:Int, y:Int, map_x:Int = 0, map_y:Int = 0) {
  def neighboursOpenMap(size_x:Int, size_y:Int):List[Coordinate] = {
    List(
      Coordinate(x - 1, y, map_x, map_y),
      Coordinate(x + 1, y, map_x, map_y),
      Coordinate(x, y - 1, map_x, map_y),
      Coordinate(x, y + 1, map_x, map_y)
    ).map{
      case Coordinate(_x, _y, _, _) if _x < 0 => Coordinate(_x + size_x, _y, map_x - 1, map_y)
      case Coordinate(_x, _y, _, _) if _x >= size_x => Coordinate(_x - size_x, _y, map_x + 1, map_y)
      case Coordinate(_x, _y, _, _) if _y < 0 => Coordinate(_x, _y + size_x, map_x, map_y - 1)
      case Coordinate(_x, _y, _, _) if _y >= size_y => Coordinate(_x, _y - size_y, map_x, map_y + 1)
      case normalCoordinate => normalCoordinate
    }
  }

  def neighboursClosedMap(size_x:Int, size_y:Int): List[Coordinate] = {
    List(
      Coordinate(x - 1, y, map_x, map_y),
      Coordinate(x + 1, y, map_x, map_y),
      Coordinate(x, y - 1, map_x, map_y),
      Coordinate(x, y + 1, map_x, map_y)
    ).filter{
      case Coordinate(x, y, _, _) => !(x < 0 || x >= size_x || y < 0 || y >= size_y)
    }
  }
}

object Coordinate {
  implicit val ordering: Ordering[Coordinate] =
    (x: Coordinate, y: Coordinate) => {
      if (x.map_x > y.map_x) 1
      else if (x.map_x < y.map_x) -1
      else if (x.map_y > y.map_y) 1
      else if (x.map_y < y.map_y) -1
      else if (x.x > y.x) 1
      else if (x.x < y.x) -1
      else if (x.y > y.y) 1
      else if (x.y < y.y) -1
      else 0
    }
}