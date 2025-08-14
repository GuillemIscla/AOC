package year_2024.day_003

object Main1 extends App {

  val input = Parser.readInput(isSample = true)

  println(input.map{
    case mul:Mul => mul.result
    case _ => 0
  }.sum)

}
