package year_2023.day_025

import scala.annotation.tailrec


case class ComponentBuilder(name:String){
  var wired:List[String] = List.empty
  def addComponent(componentName: String):Unit =
    wired = componentName :: wired
  def toComponent:Component = Component(name, wired)
}

case class Component(name:String, wired:List[String])

case class Apparatus(components:List[Component]) {
  def disconnect(wire:(String, String)):Apparatus = {
    val (nameA, nameB) = wire
    Apparatus(components.map{
      case Component(name, wired) if name == nameA || name == nameB =>
        Component(name, wired.filter(wire => wire != nameA && wire != nameB))
      case otherComponent => otherComponent
    })
  }
  def groups:List[Set[String]] = {
    components.map(_.name) match {
      case head :: tail =>
        groupsInternal(tail, toVisit = Set(head), explored = Set.empty, groups = List.empty)
      case other =>
        other.map(component => Set(component))
    }
  }

  @tailrec
  private def groupsInternal(remaining:List[String], toVisit:Set[String], explored:Set[String], groups:List[Set[String]]):List[Set[String]] = {
    toVisit.toList match {
      case toVisitHead :: toVisitTail =>
        val connected = components.find(_.name == toVisitHead).toList.flatMap(_.wired)
        val newRemaining = remaining.filterNot(connected.contains)
        val newToVisit = (toVisitTail ++ connected).filterNot(explored.contains)
        val newExplored = explored + toVisitHead
        groupsInternal(newRemaining, newToVisit.toSet, newExplored, groups)
      case Nil =>
        val newGroups = explored :: groups
        remaining match {
          case remainingHead :: remainingTail =>
            groupsInternal(remainingTail, Set(remainingHead), explored = Set.empty, newGroups)
          case Nil =>
            newGroups
        }
    }
  }

//  def stoerWagnerUntil(cutSize:Int):List[(String, String)] = {
//    def unify(allNodes:List[Component]):(Component, List[Component]) = ???
//    val (newSuperNode, restNodes) = unify(components)
//  }
}
