package year_2023.day_017


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

