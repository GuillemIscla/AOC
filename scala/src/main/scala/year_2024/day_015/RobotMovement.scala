package year_2024.day_015

trait RobotMovement {
  def toChar:Char
  def offset:(Int, Int)
}

case object Up extends RobotMovement{
  def toChar:Char = '^'
  def offset:(Int, Int) = (-1, 0)
}

case object Down extends RobotMovement{
  def toChar:Char = 'v'
  def offset: (Int, Int) = (1, 0)
}

case object Left extends RobotMovement{
  def toChar:Char = '<'
  def offset: (Int, Int) = (0, -1)
}

case object Right extends RobotMovement{
  def toChar:Char = '>'
  def offset: (Int, Int) = (0, 1)
}

object RobotMovement {
  def fromChar(raw:Char):RobotMovement =
    all.find(_.toChar == raw).get

  def all:List[RobotMovement] =
    List(Up, Down, Left, Right)
}
