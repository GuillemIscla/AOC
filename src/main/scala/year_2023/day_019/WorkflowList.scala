package year_2023.day_019

import scala.collection.immutable.List

case class GearPart(x:Int, m:Int, a:Int, s:Int) {
  val value:Int = x  + m + a + s
}

case class GearPartRange(start_x:Int, end_x:Int, start_m:Int, end_m:Int, start_a:Int, end_a:Int, start_s:Int, end_s:Int){
  def combinations:Long = (end_x.toLong - start_x.toLong) * (end_m.toLong - start_m.toLong) * (end_a.toLong - start_a.toLong) * (end_s.toLong - start_s.toLong)
  def render:List[GearPart] =
    (for {
      x <- start_x until end_x
      m <- start_m until end_m
      a <- start_a until end_a
      s <- start_s until end_s
    } yield GearPart(x, m, a, s)).toList

}

trait WComparison {
  def raw:Char
}
case object Gt extends WComparison {
  override def raw: Char = '>'
}

case object Lt extends WComparison {
  override def raw: Char = '<'
}

object WComparison {

  val all:List[WComparison] = List(Gt, Lt)
  def fromRaw(raw:Char):WComparison = all.find(_.raw == raw).getOrElse(throw new Exception(s"Could not parse GComparison with raw '$raw'"))
}

trait GoTo {
  def raw:String
}

trait FinalGoTo extends GoTo

case object Accept extends FinalGoTo {
  override def raw:String = "A"
}

case object Reject extends FinalGoTo {
  override def raw:String = "R"
}
case class GoToWorkflow(raw:String) extends GoTo

object GoTo {

  val allFinal:List[FinalGoTo] = List(Accept, Reject)
  def fromRaw(raw:String):GoTo = allFinal.find(_.raw == raw).getOrElse(GoToWorkflow(raw))
  def fromRawFinal(raw:String):FinalGoTo = allFinal.find(_.raw == raw).getOrElse(throw new Exception(s"Could not parse FinalGoTo with raw: '$raw'"))
}
case class IfThen(gearLabel:Char, gComparison: WComparison, value:Int, goTo: GoTo) {
  def compare(gearPart: GearPart): Boolean = {
    val gearValue = getFocusedValue(gearPart)
    gComparison match {
      case Gt => gearValue > value
      case Lt => gearValue < value
    }
  }
  def divideRange(gearPartRange: GearPartRange):List[(GearPartRange, Option[GoTo])] = (gearLabel, gComparison) match {
    case ('x', Gt) =>
      if(gearPartRange.end_x < value){
        List((gearPartRange, None))
      }
      else if(gearPartRange.start_x > value){
        List((gearPartRange, Some(goTo)))
      }
      else {
        List(
          (gearPartRange.copy(start_x = value), Some(goTo)),
          (gearPartRange.copy(end_x = value), None)
        )
      }
    case ('x', Lt) =>
      if (gearPartRange.start_x > value) {
        List((gearPartRange, None))
      }
      else if (gearPartRange.end_x < value) {
        List((gearPartRange, Some(goTo)))
      }
      else {
        List(
          (gearPartRange.copy(end_x = value -1), Some(goTo)),
          (gearPartRange.copy(start_x = value -1), None)
        )
      }
    case ('m', Gt) =>
      if (gearPartRange.end_m < value) {
        List((gearPartRange, None))
      }
      else if (gearPartRange.start_m > value) {
        List((gearPartRange, Some(goTo)))
      }
      else {
        List(
          (gearPartRange.copy(start_m = value), Some(goTo)),
          (gearPartRange.copy(end_m = value), None)
        )
      }
    case ('m', Lt) =>
      if (gearPartRange.start_m > value) {
        List((gearPartRange, None))
      }
      else if (gearPartRange.end_m < value) {
        List((gearPartRange, Some(goTo)))
      }
      else {
        List(
          (gearPartRange.copy(end_m = value -1), Some(goTo)),
          (gearPartRange.copy(start_m = value -1), None)
        )
      }
    case ('a', Gt) =>
      if (gearPartRange.end_a < value) {
        List((gearPartRange, None))
      }
      else if (gearPartRange.start_a > value) {
        List((gearPartRange, Some(goTo)))
      }
      else {
        List(
          (gearPartRange.copy(start_a = value), Some(goTo)),
          (gearPartRange.copy(end_a = value), None)
        )
      }
    case ('a', Lt) =>
      if (gearPartRange.start_a > value) {
        List((gearPartRange, None))
      }
      else if (gearPartRange.end_a < value) {
        List((gearPartRange, Some(goTo)))
      }
      else {
        List(
          (gearPartRange.copy(end_a = value -1), Some(goTo)),
          (gearPartRange.copy(start_a = value -1), None)
        )
      }
    case ('s', Gt) =>
      if (gearPartRange.end_s < value) {
        List((gearPartRange, None))
      }
      else if (gearPartRange.start_s > value) {
        List((gearPartRange, Some(goTo)))
      }
      else {
        List(
          (gearPartRange.copy(start_s = value), Some(goTo)),
          (gearPartRange.copy(end_s = value), None)
        )
      }
    case ('s', Lt) =>
      if (gearPartRange.start_s > value) {
        List((gearPartRange, None))
      }
      else if (gearPartRange.end_s < value) {
        List((gearPartRange, Some(goTo)))
      }
      else {
        List(
          (gearPartRange.copy(end_s = value -1), Some(goTo)),
          (gearPartRange.copy(start_s = value -1), None)
        )
      }
  }

  def getFocusedValue(gearPart: GearPart):Int = gearLabel match {
    case 'x' => gearPart.x
    case 'm' => gearPart.m
    case 'a' => gearPart.a
    case 's' => gearPart.s
  }
}
case class Workflow(label:String, ifThens:List[IfThen], end:GoTo){
  def processGear(gearPart: GearPart):GoTo = {
    ifThens.find(_.compare(gearPart)) match {
      case Some(ifThen) => ifThen.goTo
      case None => end
    }
  }

  def divideRange(gearPartRange: GearPartRange):List[(GearPartRange, GoTo)] = {
    ifThens.foldLeft(List((gearPartRange, Option.empty[GoTo]))){
      case (ranges, newIfThen) =>
        val decidedRanges = ranges.filter(_._2.isDefined)
        val undecidedRanges = ranges.filter(_._2.isEmpty)
        decidedRanges ++ undecidedRanges.flatMap{ case (range, _) => newIfThen.divideRange(range) }

    }.map{
      case (gearPartRange: GearPartRange, Some(decided)) =>
        (gearPartRange, decided)
      case (gearPartRange: GearPartRange, None) =>
        (gearPartRange, end)
    }
  }
}
case class WorkflowList(workflows: List[Workflow]) {

  def divideRange(gearPartRange: GearPartRange):List[(GearPartRange, GoTo)] = {
    var rangesGoTo = List[(GearPartRange, GoTo)]((gearPartRange, GoToWorkflow("in")))
    var finish = false

    while (!finish) {
      rangesGoTo = rangesGoTo.flatMap{
        case (range, GoToWorkflow(workflowLabel)) =>
          workflows
            .find(_.label == workflowLabel).get.divideRange(range)
        case (range, acceptReject) =>
          List((range, acceptReject))
      }
      finish = !rangesGoTo.exists{
        case (_, _:GoToWorkflow) => true
        case _ => false
      }
    }
    rangesGoTo
  }

  def processGear(gearPart: GearPart):GoTo = {
    var currentGoTo:GoTo = GoToWorkflow("in")

    while (currentGoTo != Accept && currentGoTo != Reject) {
      currentGoTo = currentGoTo match {
        case GoToWorkflow(workflowLabel) =>
          workflows.find(_.label == workflowLabel).get.processGear(gearPart)
        case other => other
      }
    }
    currentGoTo
  }

}
