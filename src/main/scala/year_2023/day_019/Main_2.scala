package year_2023.day_019

object Main_2 extends App {
  val (workflowList, _) = Parser.readInput(isSample = false)

  val allRanges = GearPartRange(0, 4000, 0, 4000, 0, 4000, 0, 4000)

  println(workflowList.divideRange(allRanges).map{
    case (range, Accept) => range.combinations
    case _ => 0
  }.sum)


}
