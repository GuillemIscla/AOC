package year_2024.day_014

object Main1 extends App {

  val input = Parser.readInput(isSample = false)


  println(input
    .flatMap(_.move(100).getQuadrant)
      .groupBy(a => a)
      .values.map(_.size).product)

}
