package year_2024.day_015

case class Warehouse(raw:List[List[WarehouseElement]], robotPositionFromInput:Option[(Int, Int)] = None){
  override def toString:String = raw.map(_.map(_.toChar).mkString).mkString("\n")

  val width:Int = raw.head.size
  val height:Int = raw.size

  val (robot_x, robot_y):(Int, Int) = robotPositionFromInput.getOrElse{
    raw.zipWithIndex.flatMap {
      case (row, i) =>
        val j = row.indexOf(Robot)
        Option.when(j >= 0)((i, j))
    }.head
  }

  def gpsCoordinates:List[(Int, Int, Int)] = {
    raw.zipWithIndex.flatMap{
      case (row, i) =>
        row.zipWithIndex.map {
          case (warehouseElement, j) => (i, j, warehouseElement.gpsCoordinate(i, j, width, height))
        }
    }
  }

  def move(robotMovement: RobotMovement, wide:Boolean):Warehouse = {
    val moveFirst =
      if (wide) raw(robot_x)(robot_y).moveWide { case (x, y) => raw(x)(y) }((robot_x, robot_y), robotMovement.offset, fromOtherSide = false)
      else raw(robot_x)(robot_y).move { case (x, y) => raw(x)(y) }((robot_x, robot_y), robotMovement.offset)
    moveFirst match {
      case Some(movements) =>
        Warehouse(raw.zipWithIndex.map{
          case (row, i) =>
            row.zipWithIndex.map{
              case (warehouseElement, j) =>
                movements.find{
                  case (_, (moved_i, moved_j)) => i == moved_i && j == moved_j
                } match {
                  case Some((movedWarehouseElement, _)) => movedWarehouseElement
                  case None => warehouseElement
                }
            }
        }, movements.find(_._1 == Robot).map(_._2))
      case None => this
    }
  }

  def toWide:Warehouse = Warehouse(raw.map {
    row => row.map(_.toWide).flatMap {
      case (left, right) => List(left, right)
    }
  })
}