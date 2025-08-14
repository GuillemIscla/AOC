package year_2023.day_023

object Main_2 extends App {
  val hikingMap = Parser.readInput(isSample = false)
  hikingMap.buildAndLinkHikingNodes(slippery = false)

  println(findLongestPath(hikingMap.startNode, hikingMap.endNode, List.empty).map(_ + 1))

  /**
   * General solution is NP hard but here just regular DFS suffices to get it soon
   * */

  def findLongestPath(origin:HikingNode, target:HikingNode, visited:List[Coordinate]):Option[Int] = {
    if(origin == target) Some(0)
    else {
      origin
        .nextNodes
        .filterNot(nextNode => visited.contains(nextNode._1.coordinate))
        .flatMap {
          case (nextNode, distance) =>
            findLongestPath(nextNode, target, visited :+ origin.coordinate)
            .map(_ + distance)
        }.maxOption
    }
  }


}
