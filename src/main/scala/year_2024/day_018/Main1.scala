package year_2024.day_018

// 138 too low

object Main1 extends App {

  val memoryRegion = Parser.readInput(isSample = false)

  val memoryRegionAfterFall = memoryRegion.fallBytes(1024)

 val endNode = memoryRegionAfterFall.findNodes(
    toExplore = List(ReachingCost(position = (0, 0), isEnd = false, trace = List((0, 0)))),
    explored = List.empty
  ).find(_.isEnd).get

  memoryRegionAfterFall.printMap(endNode.trace)
  println(endNode.trace.distinct.size -1)


}
