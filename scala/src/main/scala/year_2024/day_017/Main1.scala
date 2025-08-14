package year_2024.day_017


object Main1 extends App {

  val input = Parser.readInput(isSample = None)

  println(input.execute()._2.mkString(","))

}
