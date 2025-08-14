package year_2024.day_010

case class TopographicMap(raw:List[List[Int]]){
  val width:Int = raw.head.size
  val height:Int = raw.size

  def getTrailHeads:List[(Int, Int)] = {
    raw.zipWithIndex.flatMap {
      case (row, i) => row.zipWithIndex.flatMap {
        case (0, j) => Some((i, j))
        case _ => None
      }
    }
  }

  def getNextStep(before:(Int, Int), nextHeight:Int):List[(Int, Int)] = {
    val (i, j) = before
    List((i-1,j),
      (i+1,j),
      (i,j-1),
      (i,j+1))
      .filter{
        case (next_i, next_j) =>
          next_i >= 0 && next_i < width && next_j >= 0 && next_j < height
      }.filter{
        case (next_i, next_j) => raw(next_i)(next_j) == nextHeight
      }
  }
}
