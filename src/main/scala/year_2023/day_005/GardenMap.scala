package year_2023.day_005


trait Seeds {
  def longList:List[Long]
  def pairList:List[(Long, Long)]
}

case class ConsecutiveSeeds(longList:List[Long]) extends Seeds {
  override def pairList: List[(Long, Long)] = throw new Exception("Cannot make pairList in ConsecutiveSeeds")
}
case class RangeSeeds(pairList:List[(Long, Long)]) extends Seeds{
  override def longList: List[Long] = throw new Exception("Cannot make long list in RangeSeeds")
}

case class MapSegment(destinationRangeStart:Long, sourceRangeStart:Long, rangeLength:Long)

case class GardenMap(from:String, to:String, mapSegments:List[MapSegment]) {
  override def toString: String = s"$from-to-$to"
}

case class MapChain(maps:List[GardenMap]) {
  def chainInput(input:Long):Long = {
    maps.foldLeft(input){
      case (acc, gardenMap) =>
        gardenMap.mapSegments.find(
            mapSegment =>
              mapSegment.sourceRangeStart <= acc && mapSegment.sourceRangeStart + mapSegment.rangeLength > acc
          ).map{
            mapSegment => acc - mapSegment.sourceRangeStart + mapSegment.destinationRangeStart
          }
          .getOrElse(acc)
    }
  }
}

object MapChain {
  def fromUnsorted(gardenMaps:List[GardenMap]):MapChain = MapChain(maps = growPipe(gardenMaps)("seed"))


  private def growPipe(total:List[GardenMap])(from:String, acc:List[GardenMap] = Nil):List[GardenMap] = {
    val next = total.find(_.from == from)
    val newAcc = acc ++ next
    if(next.map(_.to).contains("location")) newAcc
    else growPipe(total)(next.get.to, newAcc)

  }

}