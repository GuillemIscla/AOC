package year_2024.day_014

case class BathroomRobot(areaWidth:Int, areaHeight:Int, px:Int, py:Int, vx:Int, vy:Int){
  def getQuadrant:Option[Int] = {
    if(px == ((areaWidth - 1) / 2) || py == ((areaHeight -1) / 2)) None
    else {
      if(px < (areaWidth - 1) / 2 && py < (areaHeight -1) / 2) Some(1)
      else if (px < (areaWidth - 1) / 2 && py > (areaHeight -1) / 2) Some(2)
      else if (px > (areaWidth - 1) / 2 && py < (areaHeight -1) / 2) Some(3)
      else Some(4)
    }
  }
  def move(seconds:Int):BathroomRobot = {
    val newPx = ((seconds * vx) + px) % areaWidth
    val newPy = ((seconds * vy) + py) % areaHeight
    BathroomRobot(
      areaWidth, areaHeight,
      px = if(newPx < 0) newPx + areaWidth else newPx,
      py = if(newPy < 0) newPy + areaHeight else newPy,
      vx, vy
    )
  }
}
