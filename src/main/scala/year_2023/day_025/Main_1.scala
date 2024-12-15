package year_2023.day_025

object Main_1 extends App {
  val apparatus = Parser.readInput(isSample = true)

  println(apparatus.components.map(c => (c, c.wired)).mkString("\n"))


}
