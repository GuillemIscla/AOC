package year_2023.day_021

trait GardenSpot {
  def raw:Char

  override def toString: String = raw.toString
}

case class Start() extends GardenSpot {
  val raw:Char = 'S'
}

case class Plot(visited:Boolean = false) extends GardenSpot {
  val raw:Char = '.'

  override def toString: String = if(visited) "O" else "."
}

case class Rock() extends GardenSpot {
  val raw:Char = '#'
}

object GardenSpot {

  val all:List[GardenSpot] = List(Start(), Plot(), Rock())


  def fromRaw(raw:Char):GardenSpot = all.find(_.raw == raw).getOrElse(throw new Exception(s"Couldn't find garden spot with raw value '$raw'"))
}

case class Garden(schema:List[List[GardenSpot]]){
  override def toString: String = schema.map(_.mkString).mkString("\n")

  val start:Coordinate = schema.zipWithIndex.flatMap{
    case (row, x) => row.zipWithIndex.find(_._1 == Start()).map{case (_, y) => Coordinate(x, y)}
  }.headOption.getOrElse(throw new Exception(s"Couldn't find start"))
}
