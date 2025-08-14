package year_2024.day_006

object Main1 extends App {

  val map = Parser.readInput(isSample = true)

  while(!isOut(map.move()) ){}

  def isOut(moveOutput: MoveOutput):Boolean = moveOutput match {
    case _:Out => true
    case _ => false
  }
  println(map.countVisited)

}
