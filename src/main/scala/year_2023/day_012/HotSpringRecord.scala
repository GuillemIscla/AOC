package year_2023.day_012

import scala.collection.mutable
import scala.util.{Success, Try}


trait HotSpringCondition {
  def raw:Char
}

case object Operational extends HotSpringCondition {
  override def raw: Char = '.'
}

case object Damaged extends HotSpringCondition {
  override def raw: Char = '#'
}

case object Unknown extends HotSpringCondition {
  override def raw: Char = '?'
}

object HotSpringCondition {
  val all:List[HotSpringCondition] = List(Operational, Damaged, Unknown)

  def fromRaw(raw:Char):HotSpringCondition = all.find(_.raw == raw).getOrElse(throw new Exception(s"Cannot parse Hotspring Condition with the raw character '$raw'"))
}


case class PlaceToStart(beforeShouldStartAtOrLater:Int, thisStartsAt:Int, nextCanStartAt:Int)
case class ArrangementResult(canStartFrom:Map[Int, Long]) {

  val total:Long = canStartFrom.values.sum
  def add(other:ArrangementResult):ArrangementResult = {
    val keys = canStartFrom.keys ++ other.canStartFrom.keys
    ArrangementResult(keys.map{
      key => key -> (canStartFrom.getOrElse(key, 0L) + other.canStartFrom.getOrElse(key, 0L))
    }.toMap)
  }

  def addPlaceToStart(placeToStart:PlaceToStart):ArrangementResult = {
    ArrangementResult(
      canStartFrom.toList.flatMap {
        case (nextCanStartFrom, count) if placeToStart.beforeShouldStartAtOrLater <= nextCanStartFrom && placeToStart.thisStartsAt >= nextCanStartFrom =>
          Some(placeToStart.nextCanStartAt -> count)
        case _ =>
          None
      }.groupBy(_._1).view.mapValues(_.map(_._2).sum).toMap
    )
  }
  def addPlacesToStart(placesToStart:List[PlaceToStart]):ArrangementResult = {
    placesToStart.map(addPlaceToStart).foldLeft(ArrangementResult(Map.empty)){
      case (accResult, newResult) =>
        val keys = accResult.canStartFrom.keys ++ newResult.canStartFrom.keys
        ArrangementResult(
          keys.map{
            key => key -> (accResult.canStartFrom.getOrElse(key, 0L) + newResult.canStartFrom.getOrElse(key, 0L))
          }.toMap
        )
    }
  }

}


case class HotSpringRecord(conditions:List[HotSpringCondition], damagedGroups:List[Int], lastDamaged:Boolean = false){

  def unfold:HotSpringRecord = {
    HotSpringRecord(conditions ++ List(Unknown) ++
      conditions ++ List(Unknown) ++
      conditions ++ List(Unknown) ++
      conditions ++ List(Unknown) ++
      conditions, damagedGroups ++ damagedGroups ++ damagedGroups ++ damagedGroups ++ damagedGroups)
  }

  def fastArrangements:Long = {
    val damagedGroupsWithLast = damagedGroups.zipWithIndex.map{
      case (damagedGroup, index) => (damagedGroup, index == damagedGroups.size -1)
    }
    damagedGroupsWithLast.foldLeft(ArrangementResult(Map(0 -> 1))){
      case (accArrangementResult, (newDamagedGroup, isLast)) =>
        if(accArrangementResult.canStartFrom.isEmpty){
          accArrangementResult
        }
        else {
          val firstToTry = accArrangementResult.canStartFrom.keys.min
          val placesToStart =
            (firstToTry until conditions.size).flatMap {
              i =>
                canPlace(placeAt = i, damagedGroup = newDamagedGroup, canLeaveDamagedAfter = !isLast).map {
                  case (minStartFrom, nextPlace) => PlaceToStart(minStartFrom, i, nextPlace)
                }
            }.toList
          accArrangementResult.addPlacesToStart(placesToStart)
        }
    }.total
  }

  //Returns tuple:
  // ._1 -> I can start from here. If I start before here there is a Damage not placed
  // ._2 -> Once placed I let next start from here.
  def canPlace(placeAt:Int, damagedGroup:Int, canLeaveDamagedAfter:Boolean):Option[(Int, Int)] = {
    val before = conditions.take(placeAt - 1)
    val place = conditions.slice(placeAt, placeAt + damagedGroup)
    val after = if(canLeaveDamagedAfter) conditions.drop(placeAt + damagedGroup).headOption.toList else conditions.drop(placeAt + damagedGroup)
    if(place.contains(Operational) || place.size < damagedGroup || after.contains(Damaged)) None
    else {
      val placeAfter:Int = before.zipWithIndex.findLast(_._1 == Damaged).map(pair => pair._2 + 1).getOrElse(0)
      Some(placeAfter, placeAt + damagedGroup + 1)
    }
  }

  def arrangements:ArrangementResult = {
    damagedGroups match {
      case Nil =>
        if(conditions.count(_ == Damaged) > 0) ArrangementResult(Map.empty)
        else ArrangementResult(Map((countUnknownsInTail + (if(lastDamaged) 0 else 1)) -> 1))
      case firstDamagedGroup :: _ =>
        (0 to conditions.count(_ == Unknown)).map {
          i =>
            placeInSpot(firstDamagedGroup, i).map(_.arrangements).getOrElse(ArrangementResult(Map.empty))
        }.foldLeft(ArrangementResult(Map.empty)){
          case (aux, newArrangementResult) => aux.add(newArrangementResult)
        }
    }
  }

  def placeInSpot(firstDamagedGroup:Int, spot:Int):Option[HotSpringRecord] = {
    var spotsLeft = spot
    var index = 0
    while(spotsLeft > -1 && index < conditions.size){
      val placeDamagedCandidate = conditions.slice(index, index + firstDamagedGroup)
      if(!placeDamagedCandidate.contains(Operational) &&
        placeDamagedCandidate.size == firstDamagedGroup &&
        Try(conditions(index + firstDamagedGroup)) != Success(Damaged)
      ) spotsLeft -= 1
      if(spotsLeft != -1) index += 1
    }
    if (index < conditions.size && !conditions.take(index).contains(Damaged))
      Some(HotSpringRecord(conditions.drop(index + firstDamagedGroup + 1), damagedGroups.tail, lastDamaged = conditions.size == index + firstDamagedGroup))
    else None
  }

  def countUnknownsInTail:Int = {
    var count = 0
    var finish = false
    while(count < conditions.size && !finish){
      if(conditions(conditions.size -1 - count) == Unknown){
        count += 1
      }
      else{
        finish = true
      }
    }
    count
  }

}
