package year_2024.day_019


object Main1 extends App {

  val (initialTowels, designs) = Parser.readInput(isSample = false)

  val primaryTowels = getBasicTowels(initialTowels).sortBy(_.stripes.size)

  println(designs.count(isPossible(_, primaryTowels)))

  def getBasicTowels(towels:List[Towel]):List[Towel] = {
    towels.filter{
      towel =>
        !isPossible(Design(towel.stripes), towels.filterNot(_ == towel))
    }
  }

  def isPossible(design: Design, towels: List[Towel]):Boolean = {
    if(design.stripes.isEmpty) true
    else {
      towels.filter{
        towel =>
          design.stripes.mkString.startsWith(towel.stripes.mkString)
      }.exists {
        towel =>
          isPossible(design.copy(stripes = design.stripes.drop(towel.stripes.size)), towels)
      }
    }
  }
}
