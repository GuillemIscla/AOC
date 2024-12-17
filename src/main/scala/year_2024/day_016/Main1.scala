package year_2024.day_016

import Cardinal._

object Main1 extends App {

  val reindeerMaze = Parser.readInput(isSample = None)
  val startPosition = reindeerMaze.startPosition
  val nodesReached =
    reindeerMaze.findNodes(
      toExplore = Cardinal.all.map{
        cardinal => ReachingCost(startPosition, turnCost(cardinal, East), cardinal, isEnd = false, trace = List.empty)
      }.sortBy(_.cost),
      explored = List.empty)

  println(nodesReached.filter(_.isEnd).map(_.cost).min)
}
