package year_2024.day_008

object Main2 extends App {
  val input = Parser.readInput(isSample = false)

  val antinodes = input.getAntennas.map {
    case (_, antennas) =>
      (for {
        antenna <- antennas
        otherAntenna <- antennas.filter(_ != antenna)
        antinode <- getAntinodes(antenna, otherAntenna, input.width, input.height)
      } yield antinode).distinct
  }.toList

  println(antinodes.flatten.distinct.size)

  def getAntinodes(a: (Int, Int), b: (Int, Int), width: Int, height: Int): List[(Int, Int)] = {
    val v = (b._1 - a._1, b._2 - a._2)
    val multiplier = Math.min(width,height)
    (0 to multiplier).toList.flatMap {
      m =>
        List(
          (a._1 + m * v._1, a._2 + m * v._2),
          (a._1 - m * v._1, a._2 - m * v._2)
        )
    }.filter {
      case (x, y) =>
        x >= 0 && y >= 0 && x < width && y < height
    }.distinct
  }

}
