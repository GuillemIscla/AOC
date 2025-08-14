package year_2023.day_023

trait Cardinal
case object North extends Cardinal
case object South extends Cardinal
case object East extends Cardinal
case object West extends Cardinal
case class Coordinate(x:Int, y:Int) {
  def neighbours():List[(Coordinate, Cardinal)] = {
    List(
      (Coordinate(x, y + 1), East),
      (Coordinate(x, y - 1), West),
      (Coordinate(x + 1, y), South),
      (Coordinate(x - 1, y), North)
    )
  }
}
trait HikingTile {
  def raw:Char

  def coordinate:Coordinate

}

case class Path(coordinate: Coordinate) extends HikingTile {
  def raw:Char = '.'
}

case class SlopeUp(coordinate: Coordinate) extends HikingTile {
  def raw:Char = '^'
}

case class SlopeDown(coordinate: Coordinate) extends HikingTile {
  def raw:Char = 'v'
}

case class SlopeLeft(coordinate: Coordinate) extends HikingTile {
  def raw:Char = '<'
}

case class SlopeRight(coordinate: Coordinate) extends HikingTile {
  def raw:Char = '>'
}

case class Forest(coordinate: Coordinate) extends HikingTile {
  def raw:Char = '#'
}

object HikingTile {

  val all:List[Coordinate => HikingTile] = List(Path.apply, SlopeUp.apply, SlopeDown.apply, SlopeRight.apply, SlopeLeft.apply, Forest.apply)
  def fromRaw(raw:Char, coordinate: Coordinate):HikingTile = all.map(f => f.apply(coordinate)).find(_.raw == raw).getOrElse(throw new Exception(s"Don't have HikingPoint with raw '$raw'"))
}