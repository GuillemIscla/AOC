package year_2023.day_023

object Main_1 extends App {
  val hikingMap = Parser.readInput(isSample = false)
  hikingMap.buildAndLinkHikingNodes(slippery = true)


  var walkingPaths:List[PathSegment] = List(PathSegment(List(hikingMap.startNode), 1))
  var finishedPaths:List[PathSegment] = List.empty

  while(walkingPaths.nonEmpty){
    val pathFrom = walkingPaths.head
    val lastNode = pathFrom.nodes.last
    val nextNodesAndDistance = lastNode.nextNodes.collect{
      case (nextNode, distance) if !pathFrom.nodes.contains(nextNode) =>
        PathSegment(pathFrom.nodes :+ nextNode, pathFrom.visitedTiles + distance)
    }
    walkingPaths = walkingPaths.tail ++ nextNodesAndDistance.filter(_.nodes.last != hikingMap.endNode)
    finishedPaths = finishedPaths ++ nextNodesAndDistance.filter(_.nodes.last == hikingMap.endNode)
  }

  println(finishedPaths.maxBy(_.visitedTiles).visitedTiles)


}
