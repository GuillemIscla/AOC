package year_2023.day_008

import scala.collection.mutable

case class Cicle(delay:Int, size:Int, zs:List[Int])

case class CamelMap(nodes:List[CamelMapNode]){
  val visited:mutable.Seq[Boolean] = mutable.Seq.from(nodes.indices.map(_ => false))
  def go(from:CamelMapNode, to:Char):CamelMapNode = {
    val result = nodes.find(n => n.name == from.goTo(to))
      .getOrElse(throw new Exception(s"In node ${from.name} going $to but there is no node ${from.goTo(to)}"))
    visited(nodes.indexOf(result)) = true
    result
  }
}


case class CamelMapNode(name:String, left:String, right:String){
  def goTo(to:Char):String = to match {
    case 'L' => left
    case 'R' => right
    case other => throw new Exception(s"Expecting 'L' or 'R' in a node but got $other")
  }
}