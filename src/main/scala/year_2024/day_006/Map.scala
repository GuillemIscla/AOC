package year_2024.day_006

import scala.collection.mutable

case class Map(raw:mutable.ListBuffer[mutable.ListBuffer[Char]]){

  private var guardPosition:(Int, Int) = {
    raw.toList.zipWithIndex.flatMap {
      case (row, i) =>
        row.toList.zipWithIndex.flatMap {
          case (char, j) if char == '^' => Some(j)
          case _ => None
        } match {
          case list if list.nonEmpty => Some((i, list.head))
          case _ => None
        }
    }.head
  }

  private var direction:Direction = Up

  val width:Int = raw.head.size
  val height:Int = raw.size

  def move():MoveOutput = {
    raw(guardPosition._1)(guardPosition._2) = 'X'
    val (next_i, next_j) = (direction, guardPosition) match {
      case (Up, (i, j)) => (i - 1, j)
      case (Down, (i, j)) => (i + 1, j)
      case (Left, (i, j)) => (i , j - 1)
      case (Right, (i, j)) => (i , j + 1)
    }

    val isOut = next_i < 0 || next_i >= width || next_j < 0  || next_j >= height
    if(isOut) Out()
    else {
      val nextIsObstacle = raw(next_i)(next_j) == '#'
      if (nextIsObstacle) {
        direction match {
          case Up => direction = Right
          case Down => direction = Left
          case Left => direction = Up
          case Right => direction = Down
        }
        Turn(guardPosition, direction)
      }
      else {
        guardPosition = (next_i, next_j)
        Moving()
      }
    }
  }

  def countVisited:Int = {
    raw.map{
      row =>
        row.count(_ == 'X')
    }.sum
  }


  def getRowsNum:Int = height
  def getObstructionsRow(row:Int):List[(Int, Int)] = {
    raw(row).toList.zipWithIndex.filter{
      case (char, _) => char == '#'
    }.map{
      case (_ , j) => (row, j)
    }
  }

  def getObstructionsCol(col: Int): List[(Int, Int)] = {
    raw.toList.map{
      row => row(col)
    }.zipWithIndex.filter {
      case (char, _) => char == '#'
    }.map {
      case (_, i) => (i, col)
    }
  }
}


trait Direction

case object Up extends Direction
case object Down extends Direction
case object Left extends Direction
case object Right extends Direction


trait MoveOutput

case class Out() extends MoveOutput
case class Moving() extends MoveOutput

case class Turn(obstacle:(Int, Int), direction: Direction) extends MoveOutput
