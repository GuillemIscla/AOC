package year_2023.day_025

case class Component(name:String, connected:Set[String])
case class MegaComponent(name:String, subComponents:Set[String]){
  def addComponent(componentName:String):MegaComponent = MegaComponent(name, subComponents + componentName)
}

case class Edge(between:(String, String), weight:Int)

case class Apparatus(nodes:List[Component]){
  def allEdges(megaNodes:Set[MegaComponent] = Set.empty)(node:String):List[Edge] = {
    val nodeToSimple =
      megaNodes
        .find(_.name == node)
        .map(_.subComponents)
        .getOrElse(Set(node))
        .flatMap(allEdgesSimpleNode)
        .groupBy(_.between._2).toList
        .map{
          case (key, grouped) =>
            Edge((node, key), grouped.toList.map(_.weight).sum)
        }
    megaNodes.foldLeft(nodeToSimple){
      case (acc, newMegaNode) =>
        acc
          .groupBy(node => newMegaNode.subComponents.contains(node.between._2))
          .foldLeft(List.empty[Edge]){
            case (acc, (false, edges)) => acc ++ edges
            case (acc, (true, edges)) => acc ++ edges.map(v => Edge((v.between._1, newMegaNode.name), v.weight))
          }
    }.distinct.filterNot(selfEdge => selfEdge.between._1 == selfEdge.between._2)
  }

  def newMegaComponent(megaNodes:Set[MegaComponent] = Set.empty)(node:String):MegaComponent =
    MegaComponent((nodes.size + megaNodes.size).toString, Set(node))

  private def allEdgesSimpleNode(node:String):Set[Edge] = {
    val connectedNodes = nodes.find(_.name == node).get.connected
    connectedNodes.map{
      connectedNode => Edge((node, connectedNode), 1)
    }
  }
}
