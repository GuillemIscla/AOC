package year_2023.day_019

object Main_1 extends App {
  val (workflowList, parts) = Parser.readInput(isSample = false)

  println(parts.filter(part => workflowList.processGear(part) == Accept).map(_.value).sum)
}
