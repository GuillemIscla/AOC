package year_2023.day_025

object Main_1 extends App {
  val apparatus = Parser.readInput(isSample = false)

  val megaComponentCut = apparatus.nodes.indices.foldLeft(Option.empty[MegaComponent]){
    case (None, i) => agglutinateForCut(apparatus.nodes(i).name, 3)
    case (foundResult, _) => foundResult
  }

  val megaNodeSize = megaComponentCut.map(_.subComponents.size).getOrElse(0)
  val restSize = apparatus.nodes.size - megaNodeSize

  println(megaNodeSize * restSize)


  /**
   * A simplified version of an iteration of the Stoer-Wagner algorithm.
   *
   * On each iteration of the algorithm it works with:
   * 1- One previously agglutinated node (MegaComponent here)
   * 2- Another node that keeps agglutinating by adding the nodes with higher weight. In some sense,
   * it adds the nodes more highly connected with the algorithm.
   * When each iteration is finished, we go and add one more node to agglutinate on 1- for the next iteration.
   *
   * These iterations guarantee that we will get the minimum cut.
   * However we already know that the minimum cut has a value of 3.
   *
   * In our case we just try agglutinating node after node until it is connected to the rest of
   * the graph by a cut of 3. We just could be (terribly) unlucky and start with a node and then agglutinate
   * first a node on the other side of the cut. We can avoid this extreme case by starting by different nodes
   * until we start with one which agglutination gets the right component that connects with a cut of 3.
   * Statistically would be very easily the first iteration. Worst of the worst case scenario would be the 7th.
   * */
  def agglutinateForCut(startNode:String, targetCut:Int):Option[MegaComponent] = {
    var iterationMegaNode = apparatus.newMegaComponent(Set.empty)(startNode)
    while(iterationMegaNode.subComponents.size < apparatus.nodes.size -1){
      val edges = apparatus.allEdges(Set(iterationMegaNode))(iterationMegaNode.name)
      if(edges.map(_.weight).sum == targetCut) return Some(iterationMegaNode)
      val newNodeToAdd = edges.maxBy(_.weight).between._2
      iterationMegaNode = iterationMegaNode.addComponent(newNodeToAdd)
    }
    None
  }
}
