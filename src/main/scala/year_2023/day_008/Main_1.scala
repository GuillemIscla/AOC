package year_2023.day_008

object Main_1 extends App {
  val (step, camelMap) = Parser.readInput(isSample = None)

  def followStep(node:CamelMapNode):(Int, CamelMapNode) = {
    step.foldLeft((0, node)){
      case ((count, accNode), newSubStep) =>
        if(node.name == "ZZZ") return (count, node)
        else (count + 1, camelMap.go(accNode, newSubStep))
    }
  }

  var currentNode = camelMap.nodes.find(_.name == "AAA").getOrElse(throw new Exception("Can't find initial node 'AAA'"))
  var stepCount = 0
  while(currentNode.name != "ZZZ"){
    val (stepsWalked, newCurrentNode) = followStep(currentNode)
    currentNode = newCurrentNode
    stepCount += stepsWalked
  }

  println(stepCount)

}
