package year_2023.day_010

import scala.io.Source

object Parser {

  def readInput(isSample:Option[Int]):PipeWeb = {
    if (isSample.contains(0)) {
        PipeWeb(parseNodes("""..F7.
        .FJ|.
        SJ.L7
        |F--J
        LJ...""".split("\n").toList))
    }
    else if(isSample.contains(1)){
      PipeWeb(parseNodes("""FF7FSF7F7F7F7F7F---7
      L|LJ||||||||||||F--J
      FL-7LJLJ||||||LJL-77
      F--JF--7||LJLJ7F7FJ-
      L---JF-JLJ.||-FJLJJ7
      |F|F-JF---7F7-L7L|7|
      |FFJF7L7F-JF7|JL---7
      7-L-JL7||F7|L7F-7F7|
      L.L7LFJ|||||FJL7||LJ
      L7JLJL-JLJLJL--JLJ.L""".split("\n").toList))
    }
    else if (isSample.contains(2)) {
      PipeWeb(parseNodes(
        """..........
        .S------7.
        .|F----7|.
        .||....||.
        .||....||.
        .|L-7F-J|.
        .|..||..|.
        .L--JL--J.
        ..........""".split("\n").toList))
    }
    else PipeWeb(parseNodes(Source.fromResource("day10_input.txt").getLines().toList))
  }

  def parseNodes(lines:List[String]):List[List[PipeNode]] =
    lines.map{
      line => line.toList.flatMap(PipeNode.fromRaw)
    }


}
