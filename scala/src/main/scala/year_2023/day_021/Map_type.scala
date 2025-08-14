package year_2023.day_021

trait Map_Type {

  def isThisType(map_x:Int, map_y:Int):Boolean
  def isOrigin:Boolean = false
  def isAxis:Boolean = false
  def sample1:(Int, Int)
  def sample2:(Int, Int)
  def waysToSum(sum:Long, isEven:Boolean):Long = {
    val list = (1 to sum.toInt).map{
      case i if (i % 2 == 1 && isEven) ||  (i % 2 == 0 && !isEven) => i - 1L
      case _ => 0
    }
    list.sum
  }
  def mapsCoordinatesAtRadius(radius: Int): List[(Int, Int)] = {
    if(radius == 0 ) List()
    else {
      (Math.max(2, radius -4) to radius).flatMap {
        j =>
          (1 until j).map {
            i => (i, j - i)
          }.toList
      }.toList
    }
  }
}

trait Map_Origin extends Map_Type {
  override def isOrigin: Boolean = true
  override def waysToSum(sum:Long, isEven:Boolean):Long = 1
  override def mapsCoordinatesAtRadius(radius: Int): List[(Int, Int)] = if(radius == 0) List((0,0)) else List.empty
}

trait Map_Axis extends Map_Type {
  override def isAxis: Boolean = true
  override def waysToSum(sum:Long, isEven:Boolean):Long = (1 to sum.toInt).map {
    case i if (i % 2 == 1 && isEven) || (i % 2 == 0 && !isEven) => 1L
    case _ => 0
  } .sum
}
case object X_0_Y_0 extends Map_Origin {
  override def sample1: (Int, Int) = (0, 0)

  override def sample2: (Int, Int) = (0, 0)

  override def isThisType(map_x: Int, map_y: Int): Boolean = map_x == 0 && map_y == 0

}
case object X_0_Y_P extends Map_Axis {
  override def sample1: (Int, Int) = (0,1)

  override def sample2: (Int, Int) = (0,2)

  override def isThisType(map_x: Int, map_y: Int): Boolean = map_x == 0 && map_y > 0

  override def mapsCoordinatesAtRadius(radius: Int): List[(Int, Int)] = {
    if (radius == 0) List.empty else (1 to radius).map(i => (0, i)).toList
  }
}
case object X_0_Y_N extends Map_Type with Map_Axis {
  override def sample1: (Int, Int) = (0,-1)

  override def sample2: (Int, Int) = (0,-2)

  override def isThisType(map_x: Int, map_y: Int): Boolean = map_x == 0 && map_y < 0

  override def mapsCoordinatesAtRadius(radius: Int): List[(Int, Int)] = {
    if(radius == 0) List.empty else (1 to radius).map(i => (0, -i)).toList
  }
}
case object X_P_Y_0 extends Map_Type with Map_Axis {
  override def sample1: (Int, Int) = (1,0)

  override def sample2: (Int, Int) = (2,0)

  override def isThisType(map_x: Int, map_y: Int): Boolean = map_x > 0 && map_y == 0
  override def mapsCoordinatesAtRadius(radius: Int): List[(Int, Int)] = {
    if (radius == 0) List.empty else (1 to radius).map(i => (i, 0)).toList
  }
}
case object X_P_Y_P extends Map_Type {
  override def sample1: (Int, Int) = (1,1)

  override def sample2: (Int, Int) = (1,2)

  override def isThisType(map_x: Int, map_y: Int): Boolean = map_x > 0 && map_y > 0

}
case object X_P_Y_N extends Map_Type {
  override def sample1: (Int, Int) = (1,-1)

  override def sample2: (Int, Int) = (2,-1)

  override def isThisType(map_x: Int, map_y: Int): Boolean = map_x > 0 && map_y < 0

  override def mapsCoordinatesAtRadius(radius: Int): List[(Int, Int)] =
    super.mapsCoordinatesAtRadius(radius).map{case (x,y) => (x,-y)}
}
case object X_N_Y_0 extends Map_Type with Map_Axis {
  override def sample1: (Int, Int) = (-1,0)

  override def sample2: (Int, Int) = (-2,0)

  override def isThisType(map_x: Int, map_y: Int): Boolean = map_x < 0 && map_y == 0

  override def mapsCoordinatesAtRadius(radius: Int): List[(Int, Int)] = {
    if (radius == 0) List.empty else (1 to radius).map(i => (-i, 0)).toList
  }
}
case object X_N_Y_P extends Map_Type {
  override def sample1: (Int, Int) = (-1,1)

  override def sample2: (Int, Int) = (-1,2)

  override def isThisType(map_x: Int, map_y: Int): Boolean = map_x < 0 && map_y > 0

  override def mapsCoordinatesAtRadius(radius: Int): List[(Int, Int)] =
    super.mapsCoordinatesAtRadius(radius).map { case (x, y) => (-x, y) }
}
case object X_N_Y_N extends Map_Type {
  override def sample1: (Int, Int) = (-1,-1)

  override def sample2: (Int, Int) = (-1,-2)

  override def isThisType(map_x: Int, map_y: Int): Boolean = map_x < 0 && map_y < 0

  override def mapsCoordinatesAtRadius(radius: Int): List[(Int, Int)] =
    super.mapsCoordinatesAtRadius(radius).map { case (x, y) => (-x, -y) }
}

object Map_Type {
  def all:List[Map_Type] = List(X_0_Y_0, X_0_Y_P, X_0_Y_N, X_P_Y_0, X_P_Y_P, X_P_Y_N, X_N_Y_0, X_N_Y_P, X_N_Y_N)

  def fromMap(coordinate:(Int, Int)):Map_Type = {
    val (map_x, map_y) = coordinate
    val newMap_x = if(map_x < 0) -1 else if(map_x > 0) 1 else 0
    val newMap_y = if(map_y < 0) -1 else if(map_y > 0) 1 else 0
    (newMap_x, newMap_y) match {
      case (0, 0) => X_0_Y_0
      case (0, 1) => X_0_Y_P
      case (0, -1) => X_0_Y_N
      case (1, 0) => X_P_Y_0
      case (1, 1) => X_P_Y_P
      case (1, -1) => X_P_Y_N
      case (-1, 0) => X_N_Y_0
      case (-1, 1) => X_N_Y_P
      case (-1, -1) => X_N_Y_N
    }
  }
}