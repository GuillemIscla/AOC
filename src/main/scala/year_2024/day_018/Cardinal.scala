package year_2024.day_018

object Cardinal extends Enumeration {
  type CardinalValue = Value
  val North, South, East, West = Value
  def newPosition(position:(Int, Int), cardinal: CardinalValue):(Int, Int) = {
    val (x, y) = position
    cardinal match {
      case North => (x - 1, y)
      case South => (x + 1, y)
      case East => (x, y + 1)
      case West => (x, y - 1)
    }
  }
  def all:List[CardinalValue] = List(North, South, East, West)

  def isOpposite(a: CardinalValue, b: CardinalValue): Boolean = {
    (a, b) match {
      case (North, South) => true
      case (South, North) => true
      case (East, West) => true
      case (West, East) => true
      case _ => false
    }
  }

  def turnCost(a: CardinalValue, b: CardinalValue): Int = {
    if (isOpposite(a, b)) 2000
    else if (a != b) 1000
    else 0
  }
}