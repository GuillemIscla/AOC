package year_2024.day_006

import scala.annotation.tailrec

object Main2 extends App {
  val isSample = false
  val map = Parser.readInput(isSample)

  val rowsNum = map.getRowsNum

  println((0 until map.width ).flatMap {
    i =>
      (0 until map.height).flatMap {
        j =>
          val newMap = Parser.readInput(isSample)
          if(newMap.raw(i)(j) == '^'){
            None
          }
          else {
            newMap.raw(i)(j) = '#'
            if(hasLoop(newMap)){
              Some((i, j))
            }
            else {
              None
            }
          }
      }
  }.size)

  @tailrec
  def hasLoop(map:Map, turned:List[Turn] = List.empty):Boolean = {
    map.move() match {
      case Out() =>
        false
      case turn: Turn if turned.contains(turn)=>
        true
      case turn:Turn =>
        hasLoop(map, turn :: turned)
      case Moving() =>
        hasLoop(map, turned)
    }

  }


}
