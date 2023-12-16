package year_2023.day_016

object Main_1 extends App {
  val contraption = Parser.readInput(isSample = false)

  val beamConfigs =
    contraption.map.indices.map {
      x => Beam(x, 0, East)
    }  ++
    contraption.map.indices.map {
      x => Beam(x, contraption.map.head.size -1, West)
    } ++
    contraption.map.head.indices.map {
      y => Beam(0, y, South)
    } ++
    contraption.map.head.indices.map {
      y => Beam(contraption.map.size - 1, y, North)
    }
  println(beamConfigs.map(bc => contraption.processBeams(List(bc)).energizedTiles).max)
}
