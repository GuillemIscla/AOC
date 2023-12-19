package year_2023.day_018

import year_2023.helper.ListUtils.TableSyntax

import scala.collection.mutable


case class LavaLagoon(schema:List[List[LavaLagoonPoint]]){

  def printSchema: String = schema.map(_.map(_.raw).mkString).mkString("\n")

  override def toString: String = schema.map(_.map(_.raw).mkString).mkString("\n")
  def fill:LavaLagoon = {
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
        case _: Trench => false
        case _ => true
      }
    }

    while (toFill.nonEmpty) {
      currentSchema =
        toFill.foldLeft(currentSchema) {
          case (accSchema, newToFill) =>
            accSchema(newToFill.x)(newToFill.y) match {
              case _:Trench => accSchema
              case _ =>
                accSchema.replaceTable(newToFill.x, newToFill.y, Terrain(filled = false))
            }

        }
      toFill = toFill.flatMap(neighboursToFill(currentSchema)).distinct
    }

    val mutFilledSchema = mutable.ListBuffer.from(currentSchema.map(mutable.ListBuffer.from))

    mutFilledSchema.indices.foreach{
      x =>
        mutFilledSchema.head.indices.foreach{
          y => if(mutFilledSchema(x)(y) == Terrain(filled = true)) mutFilledSchema(x)(y) = Trench(None)
        }
    }

    val filledSchema = List.from(mutFilledSchema.map(List.from))

    LavaLagoon(filledSchema)
  }

  private def neighboursToFill(currentSchema:List[List[LavaLagoonPoint]])(coordinate: Coordinate):List[Coordinate] = {
    List((0, 1), (0, -1), (1, 0), (-1, 0)).map {
      case (x_add, y_add) => Coordinate(coordinate.x + x_add, coordinate.y + y_add)
    }.filter{
      case Coordinate(x, _) if x < 0 || x >= currentSchema.size => false
      case Coordinate(_, y) if y < 0 || y >= currentSchema.head.size => false
      case Coordinate(x, y) =>
        currentSchema(x)(y) match {
          case _:Trench =>
            false
          case Terrain(true) =>
            true
          case _ =>
            false
        }
    }
  }

  def countTrenches:Int = schema.map(_.count{
    case _:Trench => true
    case _ => false
  }).sum
}

trait LavaLagoonPoint {
  def raw:Char

}

case class Terrain(filled:Boolean = true) extends LavaLagoonPoint {

  def raw:Char = '.'

}

case class Trench(hexColor:Option[String]) extends LavaLagoonPoint {
  def raw:Char = '#'
}

object LavaLagoonPoint {

  val all: List[LavaLagoonPoint] = List(Terrain(), Trench(None))

  def fromRaw(raw: Char): LavaLagoonPoint =
    all.find(_.raw == raw).getOrElse(throw new Exception(s"Couldn't parse lava lagoon point with raw character '$raw'"))

}