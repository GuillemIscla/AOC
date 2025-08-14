package year_2023.day_011


trait UniverseObject{
  def raw:Char
}

case object Space extends UniverseObject {
  override def toString: String = raw.toString
  override def raw: Char = '.'
}
case class Galaxy(number:Option[Int]) extends UniverseObject {
  override def toString: String = number.map(_.toString).getOrElse(raw.toString)
  override def raw: Char = '#'

}


object UniverseObject {
  def all:List[UniverseObject] = List(Space, Galaxy(None))

  def fromRaw(raw:Char):Option[UniverseObject] = all.find(_.raw == raw)
}

case class Coordinate(x:Int, y:Int, galaxyId:Int) {
  def distance(other:Coordinate):Int = Math.abs(x - other.x) + Math.abs(y - other.y)

  def expandedDistance(universeMap: UniverseMap, expansionValue:Long)(other:Coordinate):Long = {
    val rowsWithNoGalaxies:List[Int] = universeMap.rowsWithNoGalaxies
    val colsWithNoGalaxies:List[Int] = universeMap.colsWithNoGalaxies

    val rowsDistance = (Math.min(x, other.x) until Math.max(x, other.x)).map{
      case expanded if rowsWithNoGalaxies.contains(expanded) => expansionValue
      case _ => 1
    }.sum

    val colsDistance = (Math.min(y, other.y) until Math.max(y, other.y)).map{
      case expanded if colsWithNoGalaxies.contains(expanded) => expansionValue
      case _ => 1
    }.sum

    rowsDistance + colsDistance
  }

}


case class UniverseMap(rawMap:List[List[UniverseObject]]){

  val numberedMap:List[List[UniverseObject]] = {
    var i = 0
    rawMap.map{
      row => row.map{
        case Galaxy(None) =>
          i += 1
          Galaxy(Some(i))
        case other => other
      }
    }
  }

  override def toString: String = numberedMap.map(_.mkString).mkString("\n")

  def galaxies:List[Coordinate] =
    for {
      (row, rowIndex) <- numberedMap.zipWithIndex
      (universeObject, colIndex) <- row.zipWithIndex
      galaxyCoordinate <- universeObject match{
        case Galaxy(Some(id)) => Some(Coordinate(rowIndex, colIndex, id))
        case _ => None
      }
    } yield galaxyCoordinate

  def galaxyPairs:List[List[Coordinate]] = galaxies.flatMap {
    galaxy => galaxies.filter(_ != galaxy).map(other => Set(galaxy, other))
  }.distinct.map{s:Set[Coordinate] => s.toList}

  val rowsWithNoGalaxies:List[Int] = rawMap.indices.filter {
    x =>
      !rawMap(x).exists{
        case Galaxy(_) => true
        case _ => false
      }
  }.toList

  val colsWithNoGalaxies:List[Int] = rawMap.head.indices.filter {
    y => !rawMap.indices.map(x => rawMap(x)(y)).exists {
      case Galaxy(_) => true
      case _ => false
    }
  }.toList
  def smallExpand:UniverseMap = {
    val expandedRows =
      rawMap.zipWithIndex.flatMap{
        case (row, rowIndex) if rowsWithNoGalaxies.contains(rowIndex) =>
          List(row, row.indices.map(_ => Space))
        case (row, _) => List(row)
      }.map(_.toList)

    val expandedRowsAndColumns =
      expandedRows.map {
        row => row.zipWithIndex.flatMap {
          case (universeObject, colIndex) if colsWithNoGalaxies.contains(colIndex) =>
            List(universeObject, Space)
          case (universeObject, _) =>
            List(universeObject)
        }
      }
    UniverseMap(expandedRowsAndColumns)
  }

}
