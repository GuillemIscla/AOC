package year_2023.day_022

case class Coordinate(x:Int, y:Int, z:Int){
  def refByComponent(i:Int):Int = {
    if(i == 0) x
    else if(i == 1) y
    else if(i == 2) z
    else throw new Exception(s"Coordinate doesn't have a component $i")
  }

  def copyByComponent(i:Int, value:Int):Coordinate = {
    if (i == 0) copy(x = value)
    else if (i == 1) copy(y = value)
    else if (i == 2) copy(z = value)
    else throw new Exception(s"Coordinate doesn't have a component $i")
  }
}

object Coordinate {
  implicit val ordering:Ordering[Coordinate] = (x: Coordinate, y: Coordinate) => {
    if(x.x > y.x) 1
    else if(x.x < y.x) -1
    else if(x.y > y.y) 1
    else if(x.y < y.y) -1
    else if(x.z > y.z) 1
    else if(x.z < y.z) 1
    else 0
  }
}