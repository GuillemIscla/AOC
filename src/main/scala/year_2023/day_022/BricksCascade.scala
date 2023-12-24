package year_2023.day_022


case class Coordinate(x:Int, y:Int, z:Int)

case class Brick(blocks:List[Coordinate])

object Brick {
  def fromCoordinates(coord1:Coordinate, coord2:Coordinate) = ???
}
case class BricksCascade(bricks:List[Brick]) {
//  val schema:List[Lis]
  val zScanner:List[List[Brick]] = ???
}
