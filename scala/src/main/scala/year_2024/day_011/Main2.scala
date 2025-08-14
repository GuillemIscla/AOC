package year_2024.day_011

// 34840149002654 too low
//253582809724830

object Main2 extends App {

  val input = Parser.readInput(isSample = false).toWeightedStones


  val afterBlinks =
    (0 until 75).foldLeft(input) {
      case (input, i) =>
        input.blink
    }

  println(afterBlinks.stonesAndWeights.map(_._2).sum)


}
