package year_2023.day_018



trait Direction {
  def raw:Char

  def newCoordinate(coordinate: Coordinate):Coordinate
}

case object Up extends Direction {
  def raw:Char = 'U'

  override def newCoordinate(coordinate: Coordinate): Coordinate = Coordinate(coordinate.x - 1, coordinate.y)
}

case object Down extends Direction {
  def raw:Char = 'D'

  override def newCoordinate(coordinate: Coordinate): Coordinate = Coordinate(coordinate.x + 1, coordinate.y)
}

case object Left extends Direction {
  def raw:Char = 'L'

  override def newCoordinate(coordinate: Coordinate): Coordinate = Coordinate(coordinate.x, coordinate.y - 1)
}

case object Right extends Direction {
  def raw:Char = 'R'

  override def newCoordinate(coordinate: Coordinate): Coordinate = Coordinate(coordinate.x, coordinate.y + 1)
}

object Direction {

  val all:List[Direction] = List(Up, Down, Left, Right)
  def fromRaw(raw:Char):Direction =
    all.find(_.raw == raw).getOrElse(throw new Exception(s"Couldn't parse direction with raw character '$raw'"))
}


case class Coordinate(x:Int, y:Int){
  def scalarProduct(scalar:Int):Coordinate = Coordinate(x * scalar, y * scalar)
  def add(other:Coordinate):Coordinate = Coordinate(x + other.x, y + other.y)
}