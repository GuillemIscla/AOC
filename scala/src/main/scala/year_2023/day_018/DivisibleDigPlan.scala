package year_2023.day_018

import year_2023.helper.ListUtils.ListSyntax
import year_2023.helper.ListUtils.TableSyntax

case class DividingLine(isHorizontal:Boolean, justBefore:Int)
case class DivisibleDigPlan(steps:List[DigPlanStep]) {

  def dig:DivisibleLavaLagoon = {
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

    val emptyLagoonSchema:List[List[DivisibleLavaLagoonPoint]] =
      List(List(DivisibleTerrain(start_x = 0, end_x = height, start_y = 0, end_y = width, filled = true)))

    DivisibleLavaLagoon(
      steps.foldLeft((Coordinate(1 - h_3, 1 - w_3), emptyLagoonSchema)){
        case ((outerCurrentCoordinate, outerCurrentLagoon), digPlanStep: DigPlanStep) =>
          val dividingLine = getDividingLines(outerCurrentCoordinate, digPlanStep)
          var replacingLagoon =
            getDividingLines(outerCurrentCoordinate, digPlanStep).foldLeft(outerCurrentLagoon){
              case (innerCurrentLagoon, dividingLine) =>
                val result = dividingLagoonSchema(dividingLine, innerCurrentLagoon)
                result
            }
          var movingCoordinate = outerCurrentCoordinate
          val destinationCoordinate =
            outerCurrentCoordinate.add((digPlanStep.direction match {
              case Left => Coordinate(0, -1)
              case Right => Coordinate(0, 1)
              case Up => Coordinate(-1, 0)
              case Down => Coordinate(1, 0)
            }).scalarProduct(digPlanStep.deep))
          while(digPlanStep.direction match {
            case Left => movingCoordinate.y >= destinationCoordinate.y
            case Right => movingCoordinate.y <= destinationCoordinate.y
            case Up => movingCoordinate.x >= destinationCoordinate.x
            case Down => movingCoordinate.x <= destinationCoordinate.x
          } ){

            val smallCoordinate = getDivisibleTerrainSmallCoordinates(replacingLagoon, movingCoordinate)
            val divisibleLavaLagoonPoint = replacingLagoon(smallCoordinate.x)(smallCoordinate.y)
            replacingLagoon = replacingLagoon.replaceTable(smallCoordinate.x, smallCoordinate.y, DivisibleTrench(divisibleLavaLagoonPoint.start_x, divisibleLavaLagoonPoint.end_x, divisibleLavaLagoonPoint.start_y, divisibleLavaLagoonPoint.end_y))
            movingCoordinate = movingCoordinate.add(digPlanStep.direction match {
              case Left => Coordinate(0, -divisibleLavaLagoonPoint.width)
              case Right => Coordinate(0, divisibleLavaLagoonPoint.width)
              case Up => Coordinate(-divisibleLavaLagoonPoint.height, 0)
              case Down => Coordinate(divisibleLavaLagoonPoint.height, 0)
            })
          }
          (destinationCoordinate, replacingLagoon)
      }._2
    )
  }

  def getDivisibleTerrainSmallCoordinates(lavaLagoonSchema:List[List[DivisibleLavaLagoonPoint]], big:Coordinate):Coordinate = {
    val x = lavaLagoonSchema.map(_.head).zipWithIndex.find(p => p._1.start_x <= big.x && p._1.end_x > big.x).get._2
    val y = lavaLagoonSchema.head.zipWithIndex.find(p => p._1.start_y <= big.y && p._1.end_y > big.y).get._2
    Coordinate(x, y)
  }

  def getDividingLines(origin: Coordinate, digPlanStep: DigPlanStep): List[DividingLine] =
    digPlanStep.direction match {
      case Left =>
        List(
          DividingLine(isHorizontal = true, justBefore = origin.x -1),
          DividingLine(isHorizontal = true, justBefore = origin.x),
          DividingLine(isHorizontal = false, justBefore = origin.y),
          DividingLine(isHorizontal = false, justBefore = origin.y - digPlanStep.deep -1),
        )
      case Right =>
        List(
          DividingLine(isHorizontal = true, justBefore = origin.x -1),
          DividingLine(isHorizontal = true, justBefore = origin.x),
          DividingLine(isHorizontal = false, justBefore = origin.y -1),
          DividingLine(isHorizontal = false, justBefore = origin.y + digPlanStep.deep),
        )
      case Up =>
        List(
          DividingLine(isHorizontal = true, justBefore = origin.x),
          DividingLine(isHorizontal = true, justBefore = origin.x - digPlanStep.deep -1),
          DividingLine(isHorizontal = false, justBefore = origin.y),
          DividingLine(isHorizontal = false, justBefore = origin.y -1),
        )
      case Down =>
        List(
          DividingLine(isHorizontal = true, justBefore = origin.x -1),
          DividingLine(isHorizontal = true, justBefore = origin.x + digPlanStep.deep),
          DividingLine(isHorizontal = false, justBefore = origin.y),
          DividingLine(isHorizontal = false, justBefore = origin.y - 1),
        )
    }

  def dividingLagoonSchema(dividingLine: DividingLine, lagoonSchema: List[List[DivisibleLavaLagoonPoint]]): List[List[DivisibleLavaLagoonPoint]] = {
    if(!dividingLine.isHorizontal) {
      lagoonSchema.map(_.flatMap(_.verticalDivide(dividingLine)))
    }
    else {
      traverse(traverse(lagoonSchema).map(_.flatMap(_.verticalDivide(dividingLine.copy(isHorizontal = false)))))
    }
  }

  def traverse(lagoonSchema:List[List[DivisibleLavaLagoonPoint]]):List[List[DivisibleLavaLagoonPoint]] = {
    lagoonSchema.head.indices.map{
      y =>
        lagoonSchema.indices.map{
          x => lagoonSchema(x)(y).traverse
        }.toList
    }.toList
  }

}

