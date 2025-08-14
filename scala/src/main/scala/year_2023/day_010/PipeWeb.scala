package year_2023.day_010

import scala.annotation.tailrec
import scala.collection.mutable
import scala.util.Try

trait Cardinal {
  def x:Int
  def y:Int

  def opposite:Cardinal
}

case object North extends Cardinal {
  def x:Int = -1
  def y:Int = 0

  override def opposite: Cardinal = South
}
case object South extends Cardinal {
  def x:Int = 1
  def y:Int = 0

  override def opposite: Cardinal = North
}
case object East extends Cardinal {
  def x:Int = 0
  def y:Int = 1

  override def opposite: Cardinal = West
}

case object West extends Cardinal {
  def x:Int = 0
  def y:Int = -1

  override def opposite: Cardinal = East
}
case class Coordinate(x:Int, y:Int) {

  def addCardinal(cardinal: Cardinal):Coordinate = Coordinate(x + cardinal.x, y + cardinal.y)

}
trait PipeNode {
  def raw:Char
  def fromCardinal:Map[Cardinal, Cardinal]

  def isWidened:Boolean
}

case class Vertical(isWidened:Boolean = false) extends PipeNode {
  override def raw: Char = '|'
  def fromCardinal:Map[Cardinal, Cardinal] = Map(South -> North, North -> South)
}
case class Horizontal(isWidened:Boolean = false) extends PipeNode {
  override def raw: Char = '-'
  def fromCardinal:Map[Cardinal, Cardinal] = Map(East -> West, West -> East)
}
case class NE(isWidened:Boolean = false) extends PipeNode {
  override def raw: Char = 'L'
  def fromCardinal:Map[Cardinal, Cardinal] = Map(North -> East, East -> North)
}
case class NW(isWidened:Boolean = false) extends PipeNode {
  override def raw: Char = 'J'
  def fromCardinal:Map[Cardinal, Cardinal] = Map(North -> West, West -> North)
}
case class SW(isWidened:Boolean = false) extends PipeNode {
  override def raw: Char = '7'
  def fromCardinal:Map[Cardinal, Cardinal] = Map(South -> West, West -> South)
}

case class SE(isWidened:Boolean = false) extends PipeNode {
  override def raw: Char = 'F'
  def fromCardinal:Map[Cardinal, Cardinal] = Map(South -> East, East -> South)
}
case class Ground(isWidened:Boolean = false) extends PipeNode {
  override def raw: Char = '.'
  def fromCardinal:Map[Cardinal, Cardinal] = Map.empty

}
case class Start(isWidened:Boolean = false) extends PipeNode {
  override def raw: Char = 'S'
  def fromCardinal:Map[Cardinal, Cardinal] = Map.empty
}

case class Exterior(isWidened:Boolean = false) extends PipeNode {
  override def raw: Char = 'O'

  def fromCardinal: Map[Cardinal, Cardinal] = Map.empty
}

case class Interior(isWidened:Boolean = false) extends PipeNode {
  override def raw: Char = 'I'

  def fromCardinal: Map[Cardinal, Cardinal] = Map.empty
}


object PipeNode {

  val all:List[PipeNode] = List(Vertical(), Horizontal(), NE(), NW(), SE(), SW(), Ground(), Start())
  def fromRaw(raw:Char):Option[PipeNode] = all.find(_.raw == raw)
}

case class PipeWeb(nodes:List[List[PipeNode]]){

  def getPipe(coordinate: Coordinate):Option[PipeNode] = {
    val myTry = Try(nodes(coordinate.x)(coordinate.y))
    myTry.toOption
  }


  val start:Coordinate = {
    (for {
      (row, rowIndex) <- nodes.zipWithIndex
      (pipeNode, columnIndex) <- row.zipWithIndex
      if pipeNode == Start()
    } yield Coordinate(rowIndex, columnIndex))
      .headOption.getOrElse(throw new Exception("Couldn't find node start"))
  }

  def findLoop(startCardinal:Cardinal):Option[List[Coordinate]] = {
    val nextCoordinate = start.addCardinal(startCardinal)
    getPipe(nextCoordinate).flatMap(_ => findLoopInternal(startCardinal, List(start, nextCoordinate)))
  }
  @tailrec
  private def findLoopInternal(lastCardinal:Cardinal, steps:List[Coordinate] = List(start)):Option[List[Coordinate]] = {
    if(steps.size > 1 && steps.last == start) Some(steps)
    else {
      val maybeNewCardinalAndStep =
        for {
          lastPipe <- getPipe(steps.last)
          newCardinal <- lastPipe.fromCardinal.get(lastCardinal.opposite)
          newStep = steps.last.addCardinal(newCardinal)
          if !steps.contains(newStep) || newStep == start
        } yield (newCardinal, newStep)

      maybeNewCardinalAndStep match {
        case Some((_, newStep)) if newStep == start => Some(steps :+ newStep)
        case Some((newCardinal, newStep)) => findLoopInternal(newCardinal, steps :+ newStep)
        case None => None
      }
    }
  }

  def getMutableNodes:mutable.ListBuffer[mutable.ListBuffer[PipeNode]] =
    mutable.ListBuffer.from(nodes.map( rows => mutable.ListBuffer.from(rows)))


  def widen(whatIsStart:PipeNode):List[List[PipeNode]] = {
    val height = nodes.size

    val startedNodes = this.nodes.map(
      row => row.map{
        case _:Start => whatIsStart
        case other => other
      }
    )

    val widenedRows =
      (0 until height).flatMap{
        x =>
          val nextRow = Try(startedNodes(x + 1).map(Some.apply)).getOrElse(startedNodes(x).indices.map(_ => None))
          val newRow = startedNodes(x).zip(nextRow).map{
            case (_, None) => Ground(isWidened = true)
            case (Horizontal(_), _) => Ground(isWidened = true)
            case (_, Some(Horizontal(_))) => Ground(isWidened = true)
            case (Ground(_), _) => Ground(isWidened = true)
            case (_, Some(Ground(_))) => Ground(isWidened = true)
            case (Vertical(_), Some(Vertical(_))) => Vertical(isWidened = true)
            case (Vertical(_), Some(NE(_))) => Vertical(isWidened = true)
            case (Vertical(_), Some(NW(_))) => Vertical(isWidened = true)
            case (Vertical(_), Some(SW(_))) => Ground(isWidened = true)
            case (Vertical(_), Some(SE(_))) => Ground(isWidened = true)
            case (NE(_), Some(Vertical(_))) => Ground(isWidened = true)
            case (NE(_), Some(NE(_))) => Ground(isWidened = true)
            case (NE(_), Some(NW(_))) => Ground(isWidened = true)
            case (NE(_), Some(SW(_))) => Ground(isWidened = true)
            case (NE(_), Some(SE(_))) => Ground(isWidened = true)
            case (NW(_), Some(Vertical(_))) => Ground(isWidened = true)
            case (NW(_), Some(NE(_))) => Ground(isWidened = true)
            case (NW(_), Some(NW(_))) => Ground(isWidened = true)
            case (NW(_), Some(SW(_))) => Ground(isWidened = true)
            case (NW(_), Some(SE(_))) => Ground(isWidened = true)
            case (SW(_), Some(Vertical(_))) => Vertical(isWidened = true)
            case (SW(_), Some(NE(_))) => Vertical(isWidened = true)
            case (SW(_), Some(NW(_))) => Vertical(isWidened = true)
            case (SW(_), Some(SW(_))) => Ground(isWidened = true)
            case (SW(_), Some(SE(_))) => Ground(isWidened = true)
            case (SE(_), Some(Vertical(_))) => Vertical(isWidened = true)
            case (SE(_), Some(NE(_))) => Vertical(isWidened = true)
            case (SE(_), Some(NW(_))) => Vertical(isWidened = true)
            case (SE(_), Some(SW(_))) => Ground(isWidened = true)
            case (SE(_), Some(SE(_))) => Ground(isWidened = true)
            case other => throw new Exception(s"Unexpected widening $other")
          }
          List(startedNodes(x), newRow)
      }

    (0 until widenedRows.size).map {
      y =>
        val oldRow = widenedRows(y)
        (0 until widenedRows.head.size).flatMap {
          x =>
            (oldRow(x), Try(oldRow(x + 1)).toOption) match {
              case (left, None) => List(left, Ground(isWidened = true))
              case (v:Vertical, _) => List(v, Ground(isWidened = true))
              case (left, Some(_:Vertical)) => List(left, Ground(isWidened = true))
              case (Ground(myBool), _) => List(Ground(myBool), Ground(isWidened = true))
              case (left, Some(Ground(_))) => List(left, Ground(isWidened = true))
              case (h:Horizontal, Some(_:Horizontal)) => List(h, Horizontal(isWidened = true))
              case (h:Horizontal, Some(_:NE)) => List(h, Ground(isWidened = true))
              case (h:Horizontal, Some(_:NW)) => List(h, Horizontal(isWidened = true))
              case (h:Horizontal, Some(_:SW)) => List(h, Horizontal(isWidened = true))
              case (h:Horizontal, Some(_:SE)) => List(h, Ground(isWidened = true))
              case (c:NE, Some(_:Horizontal)) => List(c, Horizontal(isWidened = true))
              case (c:NE, Some(_:NE)) => List(c, Ground(isWidened = true))
              case (c:NE, Some(_:NW)) => List(c, Horizontal(isWidened = true))
              case (c:NE, Some(_:SW)) => List(c, Horizontal(isWidened = true))
              case (c:NE, Some(_:SE)) => List(c, Ground(isWidened = true))
              case (c:NW, Some(_:Horizontal)) => List(c, Ground(isWidened = true))
              case (c:NW, Some(_:NE)) => List(c, Ground(isWidened = true))
              case (c:NW, Some(_:NW)) => List(c, Ground(isWidened = true))
              case (c:NW, Some(_:SW)) => List(c, Ground(isWidened = true))
              case (c:NW, Some(_:SE)) => List(c, Ground(isWidened = true))
              case (c:SW, Some(_:Horizontal)) => List(c, Ground(isWidened = true))
              case (c:SW, Some(_:NE)) => List(c, Ground(isWidened = true))
              case (c:SW, Some(_:NW)) => List(c, Ground(isWidened = true))
              case (c:SW, Some(_:SW)) => List(c, Ground(isWidened = true))
              case (c:SW, Some(_:SE)) => List(c, Ground(isWidened = true))
              case (c:SE, Some(_:Horizontal)) => List(c, Horizontal(isWidened = true))
              case (c:SE, Some(_:NE)) => List(c, Ground(isWidened = true))
              case (c:SE, Some(_:NW)) => List(c, Horizontal(isWidened = true))
              case (c:SE, Some(_:SW)) => List(c, Horizontal(isWidened = true))
              case (c:SE, Some(_:SE)) => List(c, Ground(isWidened = true))
              case other => throw new Exception(s"Unexpected widening $other")
            }
        }.toList
    }.toList
  }

}
