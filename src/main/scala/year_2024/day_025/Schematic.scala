package year_2024.day_025

case class Schematic(raw:List[List[Char]]){
  val witdh:Int = raw.head.size
  val height:Int = raw.size

  def isLock:Boolean = raw.head.forall(_ == '#')
  def isKey:Boolean = !isLock

  def heights:List[Int] = {
    (0 until witdh).map{
      i =>
        val column = (0 until height).map{
          j => raw(j)(i)
        }.toList
        val columnFlipped = if(isLock) column else column.reverse
        columnFlipped.indexWhere(_ == '.') - 1
    }.toList
  }
}
