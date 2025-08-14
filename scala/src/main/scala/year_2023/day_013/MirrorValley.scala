package year_2023.day_013

import scala.collection.mutable
import scala.collection.mutable.ListBuffer


trait PointInMap {
  def raw:Char
}

case object Ash extends PointInMap {
  override def raw: Char = '.'
}

case object Rocks extends PointInMap {
  override def raw: Char = '#'
}

object  PointInMap {

  def all:List[PointInMap] = List(Rocks, Ash)

  def fromRaw(raw:Char):PointInMap = all.find(_.raw == raw).getOrElse(throw new Exception(s"Unrecognized PointInMap with raw: '$raw'"))
}

case class MirrorValley(pattern:List[List[PointInMap]]) {

  override def toString: String = pattern.map(_.map(_.raw).mkString).mkString("\n")

  def summarize:Int = horizontalReflection() * 100 + verticalReflection()

  def fixSmudge(row:Int, col:Int):MirrorValley = {
    val mutablePattern:mutable.ListBuffer[mutable.ListBuffer[PointInMap]] =
      ListBuffer.from(pattern).map(ListBuffer.from)

    val smudged = mutablePattern(row)(col)
    val smudgedFixed = if(smudged == Ash) Rocks else Ash
    mutablePattern(row)(col) = smudgedFixed
    MirrorValley(
      List.from(mutablePattern).map(List.from)
    )
  }

  def trySmudges:Int = {
    var x = 0

    while(x < pattern.size){
      var y = 0
      while(y < pattern.head.size){
        val newHorizontal = fixSmudge(x, y).horizontalReflection(Some(horizontalReflection()))
        val newVertical = fixSmudge(x, y).verticalReflection(Some(verticalReflection()))
        if(newHorizontal > 0) return newHorizontal * 100
        if(newVertical > 0) return newVertical
        y += 1
      }
      x += 1
    }
    throw new Exception(s"After all the smudging I couldn't find it")
  }

  def verticalReflection(avoid:Option[Int] = None):Int =
    (1 until pattern.head.size).find {
      colIndex =>
        if(avoid.contains(colIndex)) false
        else {
          pattern.forall {
            row =>
              areMirrored(row.take(colIndex), row.drop(colIndex))
          }
        }
    }.getOrElse(0)


  def horizontalReflection(avoid:Option[Int] = None):Int = {
    val traversedHorizontalReflection = pattern.head.indices.map{
      x => pattern.indices.map( y => pattern(y)(x)).toList
    }.toList
    (1 until traversedHorizontalReflection.head.size).find {
      colIndex =>
        if(avoid.contains(colIndex)) false
        else{
          traversedHorizontalReflection.forall {
            row =>
              areMirrored(row.take(colIndex), row.drop(colIndex))
          }
        }
    }.getOrElse(0)
  }

  def areMirrored(a:List[PointInMap], b:List[PointInMap]):Boolean  = {
    val toTake = Math.min(a.size, b.size)
    a.takeRight(toTake).zip(b.take(toTake).reverse).forall { case (elem_a, elem_b) => elem_a == elem_b }
  }
}
