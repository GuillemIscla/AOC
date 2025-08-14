package year_2023.day_018

import year_2023.helper.ListUtils.TableSyntax

import scala.collection.mutable


case class DivisibleLavaLagoon(schema:List[List[DivisibleLavaLagoonPoint]]){

  def printSchema: String = schema.map(_.map(_.raw).mkString).mkString("\n")

  override def toString: String = schema.map(_.map(_.raw).mkString).mkString("\n")
  def fill:DivisibleLavaLagoon = {
    var currentSchema = schema
    var toFill:List[Coordinate] =
      currentSchema.indices.map {
        x => Coordinate(x, 0)
      }.toList ++
      currentSchema.indices.map {
        x => Coordinate(x, currentSchema.head.size -1)
      }.toList ++
      currentSchema.head.indices.map {
        y => Coordinate(0, y)
      }.toList ++
      currentSchema.head.indices.map {
        y => Coordinate(currentSchema.size -1, y)
      }.toList

    toFill = toFill.filter{
      coordinate => currentSchema(coordinate.x)(coordinate.y) match {
        case _: DivisibleTrench => false
        case _ => true
      }
    }

    while (toFill.nonEmpty) {
      currentSchema =
        toFill.foldLeft(currentSchema) {
          case (accSchema, newToFill) =>
            accSchema(newToFill.x)(newToFill.y) match {
              case _:DivisibleTrench =>
                accSchema
              case dt:DivisibleTerrain =>
                accSchema.replaceTable(newToFill.x, newToFill.y, dt.copy(filled = false))
            }

        }
      toFill = toFill.flatMap(neighboursToFill(currentSchema)).distinct
    }

    val mutFilledSchema = mutable.ListBuffer.from(currentSchema.map(mutable.ListBuffer.from))

    val filledSchema = List.from(mutFilledSchema.map(List.from))

    DivisibleLavaLagoon(filledSchema)
  }

  private def neighboursToFill(currentSchema:List[List[DivisibleLavaLagoonPoint]])(coordinate: Coordinate):List[Coordinate] = {
    List((0, 1), (0, -1), (1, 0), (-1, 0)).map {
      case (x_add, y_add) => Coordinate(coordinate.x + x_add, coordinate.y + y_add)
    }.filter{
      case Coordinate(x, _) if x < 0 || x >= currentSchema.size => false
      case Coordinate(_, y) if y < 0 || y >= currentSchema.head.size => false
      case Coordinate(x, y) =>
        currentSchema(x)(y) match {
          case _:DivisibleTrench =>
            false
          case dt:DivisibleTerrain if dt.filled =>
            true
          case _ =>
            false
        }
    }
  }

  def countTrenches:Long = schema.map(_.map{
    case trench:DivisibleTrench => trench.height.toLong * trench.width.toLong
    case _ => 0L
  }.sum).sum

  def countInterior: Long = schema.map(_.map {
    case interior: DivisibleTerrain if interior.filled => interior.height.toLong * interior.width.toLong
    case _ => 0L
  }.sum).sum
}

trait DivisibleLavaLagoonPoint {
  def raw:Char

  def start_x: Int

  def end_x: Int

  def start_y: Int

  def end_y: Int

  def width:Int = end_y - start_y

  def height:Int = end_x - start_x

  def verticalDivide(dividingLine: DividingLine):List[DivisibleLavaLagoonPoint]

  def traverse:DivisibleLavaLagoonPoint

}

case class DivisibleTerrain(start_x:Int, end_x:Int, start_y:Int, end_y:Int, filled:Boolean = true) extends DivisibleLavaLagoonPoint {

  def raw:Char = '.'

  override def traverse: DivisibleLavaLagoonPoint =
    DivisibleTerrain(start_y, end_y, start_x, end_x, filled)

  override def verticalDivide(dividingLine: DividingLine): List[DivisibleTerrain] = {
    if (start_y > dividingLine.justBefore || end_y - 1 <= dividingLine.justBefore)
      List(this)
    else
      List(copy(end_y = dividingLine.justBefore + 1), copy(start_y = dividingLine.justBefore + 1))
  }
}

case class DivisibleTrench(start_x:Int, end_x:Int, start_y:Int, end_y:Int) extends DivisibleLavaLagoonPoint {
  def raw:Char = '#'
  override def traverse: DivisibleLavaLagoonPoint = DivisibleTrench(start_y, end_y, start_x, end_x)
  override def verticalDivide(dividingLine: DividingLine): List[DivisibleTrench] = {
    if (start_y > dividingLine.justBefore || end_y - 1 <= dividingLine.justBefore)
      List(this)
    else
      List(copy(end_y = dividingLine.justBefore + 1), copy(start_y = dividingLine.justBefore + 1))
  }
}
