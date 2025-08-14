package year_2024.day_011

object Main1 extends App {

  val input = Parser.readInput(isSample = true)

  val afterBlinks =
    (0 until 25).foldLeft(input){
      case (input, _) => input.blink
    }

  println(afterBlinks.stones.size)

}
