package year_2024.day_016

import Cardinal._

object Main2 extends App {

  val reindeerMaze = Parser.readInput(isSample = None)
  val startPosition = reindeerMaze.startPosition
  val nodesReached =
    reindeerMaze.findNodes(
      toExplore = Cardinal.all.map {
        cardinal => ReachingCost(startPosition, turnCost(cardinal, East), cardinal, isEnd = false, trace = List(startPosition))
      }.sortBy(_.cost),
      explored = List.empty)


//  printMap(nodesReached.filter(_.isEnd).flatMap(_.trace).distinct)
//
//  def printMap(trace:List[(Int, Int)]):Unit = {
//    reindeerMaze.raw.zipWithIndex.foreach{
//      case (row, i) =>
//        row.zipWithIndex.foreach{
//          case (char, j) =>
//            if(trace.contains((i,j))) print('O')
//            else print(char)
//        }
//        println()
//    }
//  }

  val minCost = nodesReached.filter(_.isEnd).map(_.cost).min
  println(nodesReached.filter(_.isEnd).filter(_.cost == minCost).flatMap(_.trace).distinct.size)

}
