package year_2023.day_011

object Main_1 extends App {
  val universeMap = Parser.readInput(isSample = true)

  val expandedUniverse = universeMap.smallExpand

  println(expandedUniverse.galaxyPairs.map{
    case List(galaxy1, galaxy2) => galaxy1.distance(galaxy2)
  }.sum)

  println(expandedUniverse.galaxyPairs.map {
    case List(galaxy1, galaxy2) => (galaxy1, galaxy2, galaxy1.distance(galaxy2))
  }.mkString("\n"))

}
