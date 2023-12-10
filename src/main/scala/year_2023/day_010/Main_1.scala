package year_2023.day_010

object Main_1 extends App {
  val pipeWeb = Parser.readInput(isSample = None)

  println(pipeWeb.findLoop(North).isDefined)
//  println(pipeWeb.findLoop(South).isDefined)
  println(pipeWeb.findLoop(East).isDefined)
//  println(pipeWeb.findLoop(West).isDefined)

  val loop =
    List(North, South, East, West)
      .flatMap(cardinal => pipeWeb.findLoop(cardinal))
      .headOption
      .getOrElse(throw new Exception("Couldn't find any loop"))

  println(loop.size / 2)
}
