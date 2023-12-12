package year_2023.day_011

object Main_2 extends App {
  val universeMap = Parser.readInput(isSample = false)

  val expansionValue = 1000000L

  println(universeMap.galaxyPairs.map {
    case List(galaxy1, galaxy2) =>
      galaxy1.expandedDistance(universeMap, expansionValue)(galaxy2)
  }.sum)

}
