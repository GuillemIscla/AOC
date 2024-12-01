package year_2024.day_001

object Main1 extends App {

  val input = Parser.readInput(isSample = false)


  println(input.map{
    case (a, b) => Math.abs(a - b)
  }.sum)

}
