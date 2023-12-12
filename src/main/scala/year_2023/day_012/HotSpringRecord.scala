package year_2023.day_012

import scala.collection.mutable


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

  def fromRaw(raw:Char):HotSpringCondition = all.find(_.raw == raw).getOrElse(throw new Exception(s"Cannot parse HotspringCondition with the raw character '$raw'"))
}

case class HotSpringRecord(conditions:List[HotSpringCondition], damagedGroups:List[Int]){
  def arrangements:Int = {
    if(unknowns == 0) 1
    else{
      (0 until unknowns).map {
        i =>
          damageConditions(i).placeFirst.map(_.arrangements).getOrElse(0)
      }.sum
    }

  }

  def unknowns:Int = conditions.count(_ == Unknown)

  def placeFirst:Option[HotSpringRecord] = {
    damagedGroups.headOption match {
      case Some(firstDamagedGroup) =>
        val firstIndex = Math.min(conditions.indexOf(Operational), conditions.indexOf(Unknown))
        val placeDamaged = conditions.slice(firstIndex, firstIndex + firstDamagedGroup)
        if(placeDamaged.contains(Operational)){
          None
        }
        else{
          Some(HotSpringRecord(conditions.drop(firstIndex + firstDamagedGroup), damagedGroups.tail))
        }
      case None =>
        Some(HotSpringRecord(conditions.map{
          case Unknown => Operational
          case other => other
        }, List.empty))
    }

  }

  def damageConditions(count:Int):HotSpringRecord = {
    val conditionsMutable = mutable.ListBuffer.from(conditions)
    var damaged = 0
    while(damaged < count){
      conditionsMutable(conditionsMutable.indexOf(Unknown)) = Damaged
      damaged += 1
    }
    HotSpringRecord(List.from(conditionsMutable), damagedGroups)
  }

}
