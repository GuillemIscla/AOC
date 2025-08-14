package year_2024.day_015

trait WarehouseElement {
  def toChar:Char
  def move(mapFunc:((Int, Int)) => WarehouseElement)(position:(Int, Int), offset:(Int, Int)):Option[List[(WarehouseElement, (Int, Int))]]
  def gpsCoordinate(x:Int, y:Int, width:Int, height:Int):Int
  def toWide:(WarehouseElement, WarehouseElement)
  def moveWide(mapFunc:((Int, Int)) => WarehouseElement)(position:(Int, Int), offset:(Int, Int), fromOtherSide:Boolean):Option[List[(WarehouseElement, (Int, Int))]]
}

case object Robot extends WarehouseElement {
  override def toChar: Char = '@'

  override def move(mapFunc: ((Int, Int)) => WarehouseElement)(position:(Int, Int), offset: (Int, Int)): Option[List[(WarehouseElement, (Int, Int))]] = {
    val newPosition = (position._1 + offset._1, position._2 + offset._2)

    mapFunc(newPosition).move(mapFunc)(newPosition, offset).map{
      movements =>
        (Nothing, position) :: (Robot, newPosition) :: movements
    }
  }
  def gpsCoordinate(x:Int, y:Int, width:Int, height:Int):Int = 0

  override def toWide: (WarehouseElement, WarehouseElement) = (Robot, Nothing)

  override def moveWide(mapFunc: ((Int, Int)) => WarehouseElement)(position: (Int, Int), offset: (Int, Int), fromOtherSide:Boolean): Option[List[(WarehouseElement, (Int, Int))]] = {
    val newPosition = (position._1 + offset._1, position._2 + offset._2)

    mapFunc(newPosition).moveWide(mapFunc)(newPosition, offset, fromOtherSide = false).map {
      movements =>
        ((Nothing, position) :: (Robot, newPosition) :: movements).groupBy(_._2).values.map(_.distinct).flatMap {
          case elem :: Nil => List(elem)
          case Nil => Nil
          case list => list.find(_._1 != Nothing).toList
        }.toList
    }
  }
}

case object Wall extends WarehouseElement {
  override def toChar: Char = '#'

  override def move(mapFunc: ((Int, Int)) => WarehouseElement)(position:(Int, Int), offset: (Int, Int)): Option[List[(WarehouseElement, (Int, Int))]] =
    None

  def gpsCoordinate(x:Int, y:Int, width:Int, height:Int):Int = 0

  override def toWide: (WarehouseElement, WarehouseElement) = (Wall, Wall)

  override def moveWide(mapFunc: ((Int, Int)) => WarehouseElement)(position: (Int, Int), offset: (Int, Int), fromOtherSide:Boolean): Option[List[(WarehouseElement, (Int, Int))]] =
    None
}

case object Box extends WarehouseElement {
  override def toChar: Char = 'O'

  override def move(mapFunc: ((Int, Int)) => WarehouseElement)(position:(Int, Int), offset: (Int, Int)): Option[List[(WarehouseElement, (Int, Int))]] = {
    val newPosition = (position._1 + offset._1, position._2 + offset._2)

    mapFunc(newPosition).move(mapFunc)(newPosition, offset).map {
      movements => (Box, newPosition) :: movements
    }
  }

  def gpsCoordinate(x:Int, y:Int, width:Int, height:Int):Int = 100 * x + y

  override def toWide: (WarehouseElement, WarehouseElement) = (WideBoxLeft, WideBoxRight)

  override def moveWide(mapFunc: ((Int, Int)) => WarehouseElement)(position: (Int, Int), offset: (Int, Int), fromOtherSide:Boolean): Option[List[(WarehouseElement, (Int, Int))]] =
    None // Will not be called

}

case object WideBoxLeft extends WarehouseElement {
  override def toChar: Char = '['

  override def move(mapFunc: ((Int, Int)) => WarehouseElement)(position:(Int, Int), offset: (Int, Int)): Option[List[(WarehouseElement, (Int, Int))]] =
    None // Will not be called

  def gpsCoordinate(x:Int, y:Int, width:Int, height:Int):Int = {
    if(x > width - x - 1) 0
    else 100 * x + y
  }

  override def toWide: (WarehouseElement, WarehouseElement) = (WideBoxLeft, WideBoxLeft) // Will not be called

  override def moveWide(mapFunc: ((Int, Int)) => WarehouseElement)(position: (Int, Int), offset: (Int, Int), fromOtherSide:Boolean): Option[List[(WarehouseElement, (Int, Int))]] = {
    if(!fromOtherSide) {
      val otherSidePosition = (position._1, position._2 + 1)
      mapFunc(otherSidePosition).moveWide(mapFunc)(otherSidePosition, offset, fromOtherSide = true).flatMap {
        movements =>
          val newPosition = (position._1 + offset._1, position._2 + offset._2)

          mapFunc(newPosition).moveWide(mapFunc)(newPosition, offset, fromOtherSide = offset == Right.offset).map {
            moreMovements =>
              val otherSideNewPosition = (otherSidePosition._1 + offset._1, otherSidePosition._2 + offset._2)
              val allMovementsFiltered = (movements ++ moreMovements).filter{ //we impose that other side of the box goes to the offset
                case (_, moreMovementsPosition) =>
                  moreMovementsPosition != otherSideNewPosition &&
                    moreMovementsPosition != newPosition
              }.distinct
              (WideBoxLeft, newPosition) :: (Nothing, position) :: (WideBoxRight, otherSideNewPosition) :: (Nothing, otherSidePosition) :: allMovementsFiltered
          }
      }
    }
    else {
      val newPosition = (position._1 + offset._1, position._2 + offset._2)
      mapFunc(newPosition).moveWide(mapFunc)(newPosition, offset, fromOtherSide = offset == Right.offset)
    }
  }
}

case object WideBoxRight extends WarehouseElement {
  override def toChar: Char = ']'

  override def move(mapFunc: ((Int, Int)) => WarehouseElement)(position:(Int, Int), offset: (Int, Int)): Option[List[(WarehouseElement, (Int, Int))]] =
    None // Will not be called

  def gpsCoordinate(x:Int, y:Int, width:Int, height:Int):Int = {
    if (width - x - 1 > x) 0
    else 100 * (width - x  - 1) + y
  }

  override def toWide: (WarehouseElement, WarehouseElement) = (WideBoxRight, WideBoxRight) // Will not be called

  override def moveWide(mapFunc: ((Int, Int)) => WarehouseElement)(position: (Int, Int), offset: (Int, Int), fromOtherSide:Boolean): Option[List[(WarehouseElement, (Int, Int))]] = {
    if (!fromOtherSide) {
      val otherSidePosition = (position._1, position._2 - 1)
      mapFunc(otherSidePosition).moveWide(mapFunc)(otherSidePosition, offset, fromOtherSide = true).flatMap {
        movements =>
          val newPosition = (position._1 + offset._1, position._2 + offset._2)

          mapFunc(newPosition).moveWide(mapFunc)(newPosition, offset, fromOtherSide = offset == Right.offset).map {
            moreMovements =>
              val otherSideNewPosition = (otherSidePosition._1 + offset._1, otherSidePosition._2 + offset._2)
              val allMovementsFiltered = (movements ++ moreMovements).filter { //we impose that other side of the box goes to the offset
                case (_, moreMovementsPosition) =>
                  moreMovementsPosition != otherSideNewPosition &&
                    moreMovementsPosition != newPosition
              }.distinct
              (WideBoxRight, newPosition) :: (Nothing, position) :: (WideBoxLeft, otherSideNewPosition) :: (Nothing, otherSidePosition) :: allMovementsFiltered
          }
      }
    }
    else {
      val newPosition = (position._1 + offset._1, position._2 + offset._2)
      mapFunc(newPosition).moveWide(mapFunc)(newPosition, offset, fromOtherSide = offset == Left.offset)
    }
  }
}

case object Nothing extends WarehouseElement {
  override def toChar: Char = '.'

  override def move(mapFunc: ((Int, Int)) => WarehouseElement)(to: (Int, Int), offset: (Int, Int)): Option[List[(WarehouseElement, (Int, Int))]] =
    Some(List())

  def gpsCoordinate(x:Int, y:Int, width:Int, height:Int):Int = 0

  override def toWide: (WarehouseElement, WarehouseElement) = (Nothing, Nothing)

  override def moveWide(mapFunc: ((Int, Int)) => WarehouseElement)(position: (Int, Int), offset: (Int, Int), fromOtherSide:Boolean): Option[List[(WarehouseElement, (Int, Int))]] =
    Some(List())
}


object WarehouseElement {
  def fromChar(raw:Char):WarehouseElement =
    all.find(_.toChar == raw).get

  def all:List[WarehouseElement] =
    List(Wall, Box, Robot, Nothing, WideBoxRight, WideBoxLeft)
}