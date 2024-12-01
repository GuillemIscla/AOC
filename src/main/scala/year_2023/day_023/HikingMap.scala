package year_2023.day_023


case class PathSegment(nodes:List[HikingNode], visitedTiles:Int)

case class HikingNode(coordinate:Coordinate, var nextNodes:List[(HikingNode, Int)] = List.empty){
  override def toString: String = s"HikingNode($coordinate, nextNodes:${nextNodes.map(p => (p._1.coordinate, p._2))}"
}
case class HikingMap(schema:List[List[HikingTile]]) {
  val startNode: HikingNode = HikingNode(Coordinate(0, schema.head.collectFirst { case hikingTile if hikingTile.raw == '.' => hikingTile.coordinate.y }.get))
  val endNode: HikingNode = HikingNode(Coordinate(schema.size - 1, schema.last.collectFirst { case hikingTile if hikingTile.raw == '.' => hikingTile.coordinate.y }.get))

  def buildAndLinkHikingNodes(slippery:Boolean):List[HikingNode] = {
    var nodesExplored:List[HikingNode] = List()
    var nodesToExplore:List[HikingNode] = List(startNode)
    while(nodesToExplore.nonEmpty){
      val exploringNode = nodesToExplore.head
      nodesToExplore = nodesToExplore.tail
      val nextCoordinates = exploringNode.coordinate.neighbours().filter{
        case (neighbour, cardinal) =>
          neighbour.x > 0 && neighbour.x < schema.size &&
            neighbour.y > 0 && neighbour.y < schema.head.size &&
            checkTerrain(cardinal, schema(neighbour.x)(neighbour.y).raw, slippery)
      }
      val nextNodes =
        for {
          (coordinate, _) <- nextCoordinates
          (nextNode, distance) <- followPath(exploringNode.coordinate, coordinate, slippery)
        } yield {
          (nodesExplored ++ nodesToExplore)
            .find(_.coordinate == nextNode.coordinate) match {
              case Some(foundNode) => (foundNode, distance)
              case None =>
                nodesToExplore = nodesToExplore :+ nextNode
                (nextNode, distance)
            }
        }
      exploringNode.nextNodes = nextNodes
      nodesExplored = nodesExplored :+ exploringNode

    }  // end of while
    nodesExplored
  }

  def checkTerrain(cardinal: Cardinal, raw: Char, slippery:Boolean): Boolean = {
    (raw, cardinal) match {
      case ('#', _) => false
      case ('.', _) => true
      case ('^', North) => true
      case ('>', East) => true
      case ('v', South) => true
      case ('<', West) => true
      case _ => !slippery
    }
  }

  private def followPath(lastCoordinateInput: Coordinate, currentCoordinateInput: Coordinate, slippery:Boolean):Option[(HikingNode, Int)] = {
    var i = 0
    var lastCoordinate = lastCoordinateInput
    var currentCoordinate = currentCoordinateInput
    var destinationNode = Option.empty[HikingNode]
    var finished = false
    while(!finished){
      val neighbours = currentCoordinate.neighbours().filter {
        case (neighbour, cardinal) =>
            neighbour.x >= 0 && neighbour.x < schema.size &&
            neighbour.y >= 0 && neighbour.y < schema.head.size &&
            neighbour != lastCoordinate &&
            checkTerrain(cardinal, schema(neighbour.x)(neighbour.y).raw, slippery)
      }

      //You need to code something that makes sense here
      neighbours match {
        case List((endCoordinate, _)) if endCoordinate == endNode.coordinate =>
          destinationNode = Some(endNode)
          finished = true
        case List((onlyOneDirection, _)) =>
          lastCoordinate = currentCoordinate
          currentCoordinate = onlyOneDirection
        case Nil =>
          finished = true
        case _ =>
          destinationNode = Some(HikingNode(currentCoordinate))
          finished = true
      }
      i += 1
    }
    destinationNode.map((_, i))
  }
}
