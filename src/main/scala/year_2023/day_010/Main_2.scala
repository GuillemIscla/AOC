package year_2023.day_010

import scala.annotation.tailrec
import scala.util._

object Main_2 extends App {
  val strechedPipeWeb = Parser.readInput(isSample = None)

  val whatShouldStartBe =
    List(North, South, East, West)
      .map(cardinal => strechedPipeWeb.findLoop(cardinal).isDefined) match {
        case List(true, true, _, _) => PipeNode.fromRaw('|').get
        case List(true, false, true, _) => PipeNode.fromRaw('L').get
        case List(true, false, false, true) => PipeNode.fromRaw('J').get
        case List(false, true, true, _) => PipeNode.fromRaw('F').get
        case List(false, true, false, true) => PipeNode.fromRaw('7').get
        case List(false, false, true, true) => PipeNode.fromRaw('-').get
      }

  val widenedWithoutStart = strechedPipeWeb.widen(whatShouldStartBe)

  val widenedWithStart = widenedWithoutStart.zipWithIndex.map {
    case (row, rowIndex) => row.zipWithIndex.map {
      case (_, y) if rowIndex == strechedPipeWeb.start.x * 2 && y == strechedPipeWeb.start.y * 2 => Start()
      case (other, _) => other
    }
  }


  val widenedPipeWeb = PipeWeb(widenedWithStart)


  val loop =
    List(North, South, East, West)
      .flatMap(cardinal => widenedPipeWeb.findLoop(cardinal))
      .headOption
      .getOrElse(throw new Exception("Couldn't find any loop"))

  val mutNodes = widenedPipeWeb.getMutableNodes

  var hasPaintedExterior:Boolean = false

  def paintExterior():Unit = {
    paintExteriorInit()
    while(hasPaintedExterior){
      hasPaintedExterior = false
      paintExteriorIteration()
    }
  }
  def paintExteriorInit():Unit = {
    val width = mutNodes.head.size
    val height = mutNodes.size

    (0 until height).foreach {
      x =>
        if (mutNodes(x).head != Exterior(true) && mutNodes(x).head != Exterior(false)) if(shouldPaintExterior(Coordinate(x, 0))) paintExteriorArea(Coordinate(x,0))
        if (mutNodes(x)(width -1) != Exterior(true) && mutNodes(x)(width -1) != Exterior(false)) if(shouldPaintExterior(Coordinate(x, width -1))) paintExteriorArea(Coordinate(x, width -1))
    }

    (0 until height).foreach {
      y =>
        if (mutNodes.head(y) != Exterior(true) && mutNodes.head(y) != Exterior(false)) if(shouldPaintExterior(Coordinate(0, y))) paintExteriorArea(Coordinate(0, y))
        if (mutNodes(height - 1)(y) != Exterior(true) && mutNodes(height - 1)(y) != Exterior(false)) if(shouldPaintExterior(Coordinate(height - 1,y))) paintExteriorArea(Coordinate(height - 1,y))
    }
  }

  def paintExteriorIteration():Unit = {
    val exteriorCoors =
      for{
        (row, rowIndex) <- mutNodes.zipWithIndex
        exterior <- row.zipWithIndex.flatMap{
          case (_:Exterior, colIndex) => Some(Coordinate(rowIndex, colIndex))
          case _ => None
        }
      } yield exterior

    exteriorCoors.foreach{
      exteriorCoor =>
        List(North, South, East, West).map(exteriorCoor.addCardinal).find(shouldPaintExterior) match {
          case Some(newCoordinate) => paintExteriorArea(newCoordinate)
          case None =>
        }
    }
  }

  @tailrec
  def paintExteriorArea(coordinate: Coordinate):Unit = {
    hasPaintedExterior = true
    mutNodes(coordinate.x)(coordinate.y) match {
      case Ground(isWidened) => mutNodes(coordinate.x)(coordinate.y) = Exterior(isWidened)
      case _ => mutNodes(coordinate.x)(coordinate.y) = Exterior(isWidened = false)
    }
    List(North, South, East, West).map(coordinate.addCardinal).find(shouldPaintExterior) match {
      case Some(newCoordinate) => paintExteriorArea(newCoordinate)
      case None =>
    }
  }

  def shouldPaintExterior(coordinate: Coordinate):Boolean = {
    Try(mutNodes(coordinate.x)(coordinate.y)) != Success(Exterior(true)) &&
      Try(mutNodes(coordinate.x)(coordinate.y)) != Success(Exterior(false)) &&
      Try(mutNodes(coordinate.x)(coordinate.y)).isSuccess &&
      !loop.contains(coordinate)
  }

  def paintInterior():Unit = {
    val width = mutNodes.head.size
    val height = mutNodes.size
    (0 until height).foreach {
      x =>
        (0 until width).foreach{
          y =>
            if(mutNodes(x)(y) != Exterior(true) && mutNodes(x)(y) != Exterior(false) && !loop.contains(Coordinate(x,y))){
              mutNodes(x)(y) match {
                case node => mutNodes(x)(y) = Interior(node.isWidened)
              }
            }
        }
    }
  }


  paintExterior()
  paintInterior()
  println(mutNodes.map(_.count(p => p == Interior(false))).sum)
}
