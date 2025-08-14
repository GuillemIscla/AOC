package year_2023.day_008

object Main_2 extends App {
  val (step, camelMap) = Parser.readInput(isSample = None)

  println(step.size)

  def followStep(nodes: List[CamelMapNode]): (Int, List[CamelMapNode]) = {
    step.foldLeft((0, nodes)) {
      case ((count, accNodes), newSubStep) =>
        if (accNodes.forall(_.name.last == 'Z')) return (count, accNodes)
        else (count + 1, accNodes.map(accNode => camelMap.go(accNode, newSubStep)))
    }
  }

  var currentNodes = camelMap.nodes.filter(_.name.last == 'A')

  var stepCount = 0
  var i = 0
  while (!currentNodes.forall(_.name.last == 'Z')) {
    val (stepsWalked, newCurrentNodes) = followStep(currentNodes)
    if(currentNodes.map(_.name.last).count(_ == 'Z') > 0){
      val zs = currentNodes.zipWithIndex.flatMap {
        case (node, index) if node.name.last == 'Z' => Some(index)
        case _ => None
      }
      if(zs.contains(2))
      println(s"${i.toDouble/71}: ${zs}")
    }
    currentNodes = newCurrentNodes
    stepCount += stepsWalked
    i += 1
  }

  println(stepCount)

  println

}
