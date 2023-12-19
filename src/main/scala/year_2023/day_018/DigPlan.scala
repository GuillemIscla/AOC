package year_2023.day_018

import year_2023.helper.ListUtils.ListSyntax
import year_2023.helper.ListUtils.TableSyntax

case class DigPlanStep(direction: Direction, deep:Int, hexColor:String) {
  def fromHex:DigPlanStep = {
    val direction =
      hexColor.last.toString.toInt match {
        case 0 => Direction.fromRaw('R')
        case 1 => Direction.fromRaw('D')
        case 2 => Direction.fromRaw('L')
        case 3 => Direction.fromRaw('U')
      }
    val deep =
      Integer.parseInt(hexColor.drop(1).dropRight(1), 16)
    DigPlanStep(direction, deep, hexColor)
  }
}

case class DigPlan(steps:List[DigPlanStep]) {

  def fromHex:DivisibleDigPlan = DivisibleDigPlan(steps.map(_.fromHex))
  def dig:LavaLagoon = {
    val (_, w_2, w_3) = steps.foldLeft((1, 1, 1)){
      case ((accCurrent, accMax, accMin), DigPlanStep(Left, deep, _)) =>
        val newCurrent = accCurrent - deep
        (newCurrent, Math.max(accMax, newCurrent), Math.min(accMin, newCurrent))
      case ((accCurrent, accMax, accMin), DigPlanStep(Right, deep, _)) =>
        val newCurrent = accCurrent + deep
        (newCurrent, Math.max(accMax, newCurrent), Math.min(accMin, newCurrent))
      case (acc, _) => acc
    }

    val (_, h_2, h_3) = steps.foldLeft((1, 1, 1)) {
      case ((accCurrent, accMax, accMin), DigPlanStep(Up, deep, _)) =>
        val newCurrent = accCurrent - deep
        (newCurrent, Math.max(accMax, newCurrent), Math.min(accMin, newCurrent))
      case ((accCurrent, accMax, accMin), DigPlanStep(Down, deep, _)) =>
        val newCurrent = accCurrent + deep
        (newCurrent, Math.max(accMax, newCurrent), Math.min(accMin, newCurrent))
      case (acc, _) => acc
    }

    val width = w_2 - w_3 + 1
    val height = h_2 - h_3 + 1

    val emptyLagoonSchema:List[List[LavaLagoonPoint]] =
      (0 until height).map{
      _ =>
        (0 until width).map[LavaLagoonPoint]{
          _ => Terrain()
        }.toList
    }.toList

    LavaLagoon(
      steps.foldLeft((Coordinate(1 - h_3, 1 - w_3), emptyLagoonSchema)){
        case ((outerCurrentCoordinate, outerCurrentLagoon), DigPlanStep(direction, deep, hexColor)) =>
          (0 until deep).foldLeft((outerCurrentCoordinate, outerCurrentLagoon)){
            case ((outerCurrentCoordinate, innerCurrentLagoon), _) =>
              val newCoordinate = direction.newCoordinate(outerCurrentCoordinate)
              val newLagoon = innerCurrentLagoon.replaceTable(newCoordinate.x, newCoordinate.y, Trench(Some(hexColor)))
              (newCoordinate, newLagoon)
          }
      }._2
    )
  }

}

