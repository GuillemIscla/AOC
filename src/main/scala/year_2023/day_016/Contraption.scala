package year_2023.day_016

import scala.annotation.tailrec
import scala.collection.mutable


trait Cardinal{
  def next(x:Int, y:Int):(Int, Int)
}

case object North extends Cardinal {
  override def next(x: Int, y: Int): (Int, Int) = (x - 1, y)
}
case object South extends Cardinal{
  override def next(x: Int, y: Int): (Int, Int) = (x + 1, y)
}
case object East extends Cardinal{
  override def next(x: Int, y: Int): (Int, Int) = (x, y + 1)
}
case object West extends Cardinal{
  override def next(x: Int, y: Int): (Int, Int) = (x, y - 1)
}
trait Tile { self =>
  def raw:Char
  def energy:Int

  def energize(cardinal:Cardinal):Tile

  def processBeam(cardinal: Cardinal):List[Cardinal]

  def gotBeamPassingBy:Set[Cardinal]

}

case class Empty(energy:Int = 0, gotBeamPassingBy:Set[Cardinal] = Set.empty) extends Tile {
  def raw:Char = '.'

  def energize(cardinal:Cardinal):Empty = this.copy(energy = energy + 1, gotBeamPassingBy + cardinal)

  override def processBeam(cardinal: Cardinal): List[Cardinal] = List(cardinal)
}

case class Slash(energy:Int = 0, gotBeamPassingBy:Set[Cardinal] = Set.empty) extends Tile {
  def raw:Char = '/'

  def energize(cardinal:Cardinal):Slash = this.copy(energy = energy + 1, gotBeamPassingBy + cardinal)

  override def processBeam(cardinal: Cardinal): List[Cardinal] =
    cardinal match {
      case North => List(East)
      case South => List(West)
      case East => List(North)
      case West => List(South)
    }
}

case class BackSlash(energy:Int = 0, gotBeamPassingBy:Set[Cardinal] = Set.empty) extends Tile {
  def raw:Char = '\\'

  def energize(cardinal:Cardinal):BackSlash = this.copy(energy = energy + 1, gotBeamPassingBy + cardinal)

  override def processBeam(cardinal: Cardinal): List[Cardinal] =
    cardinal match {
      case North => List(West)
      case South => List(East)
      case East => List(South)
      case West => List(North)
    }
}

case class Hyphen(energy:Int = 0, gotBeamPassingBy:Set[Cardinal] = Set.empty) extends Tile {
  def raw:Char = '-'

  def energize(cardinal:Cardinal):Hyphen = this.copy(energy = energy + 1, gotBeamPassingBy + cardinal)

  override def processBeam(cardinal: Cardinal): List[Cardinal] =
    cardinal match {
      case North => List(East, West)
      case South => List(East, West)
      case East => List(East)
      case West => List(West)
    }
}

case class Vertical(energy:Int = 0, gotBeamPassingBy:Set[Cardinal] = Set.empty) extends Tile {
  def raw:Char = '|'

  def energize(cardinal:Cardinal):Vertical = this.copy(energy = energy + 1, gotBeamPassingBy + cardinal)

  override def processBeam(cardinal: Cardinal): List[Cardinal] =
    cardinal match {
      case North => List(North)
      case South => List(South)
      case East => List(North, South)
      case West => List(North, South)
    }
}


object Tile {

  val all:List[Tile] = List(Empty(), Slash(), BackSlash(), Hyphen(), Vertical())

  def fromRaw(raw:Char):Tile = all.find(_.raw == raw).getOrElse(throw new Exception(s"Could not find tile with raw '$raw'"))
}


case class Beam(x:Int, y:Int, cardinal: Cardinal)
case class Contraption(map:List[List[Tile]]) {

  override def toString: String = map.map(_.map(_.raw).mkString).mkString("\n")

  def printEnergy: String = map.map(_.map(t => if(t.energy > 9 ) "x" else t.energy ).mkString).mkString("\n")
  @tailrec
  final def processBeams(beams:List[Beam]): Contraption = {
    if(beams.isEmpty) this
    else {
      val mutTiles = mutable.ListBuffer.from(map.map(mutable.ListBuffer.from))

      val newBeams = beams.flatMap {
        beam =>
          val tile = mutTiles(beam.x)(beam.y)
          mutTiles(beam.x)(beam.y) = tile.energize(beam.cardinal)
          tile
            .processBeam(beam.cardinal)
            .map{
              newCardinal =>
                val (newX, newY) = newCardinal.next(beam.x, beam.y)
                Beam(newX, newY, newCardinal)
            }
            .filter{
              b => tileInside(b.x, b.y) && !mutTiles(b.x)(b.y).gotBeamPassingBy.contains(b.cardinal)
            }
      }

      Contraption(List.from(mutTiles.map(List.from))).processBeams(newBeams)
    }
  }

  def tileInside(x:Int, y:Int):Boolean =
    x >= 0 && x < map.size && y >=0 && y < map.head.size

  def energizedTiles:Int = map.map(_.count(_.energy > 0)).sum

}
