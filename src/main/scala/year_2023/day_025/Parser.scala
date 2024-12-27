package year_2023.day_025

import scala.io.Source

object Parser {

  def readInput(isSample:Boolean):Apparatus = {
    if (isSample) {
      parseApparatus("""jqt: rhn xhk nvd
                       |rsh: frs pzl lsr
                       |xhk: hfx
                       |cmg: qnr nvd lhk bvb
                       |rhn: xhk bvb hfx
                       |bvb: xhk hfx
                       |pzl: lsr hfx nvd
                       |qnr: nvd
                       |ntq: jqt hfx bvb xhk
                       |nvd: lhk
                       |lsr: lhk
                       |rzs: qnr cmg lsr rsh
                       |frs: qnr lhk lsr""".stripMargin.split("\n").toList)
    }
    else parseApparatus(Source.fromResource("year_2023/day_025_input.txt").getLines().toList)
  }

  def parseApparatus(lines:List[String]):Apparatus = {
    val wiredRegex = "([a-z]+):(( [a-z]+)+)".r
    val parsedLines = lines.map {
      line =>
        val result = wiredRegex.findFirstMatchIn(line).getOrElse(throw new Exception(s"Could not parse line '$line'"))

        (result.group(1), result.group(2).tail.split(" ").toList)
    }

    val componentNames = parsedLines.flatMap { case (right, leftList) => right :: leftList }.distinct
    Apparatus(
      componentNames.map{
        componentName =>

          val edges1 = parsedLines.find(_._1 == componentName) match {
            case Some((_, connectedComponents)) => connectedComponents.toSet
            case None => Set.empty
          }
          val edges2 = parsedLines.flatMap {
            case (origin, connected) if connected.contains(componentName) => Some(origin)
            case _ => None
          }.toSet

          Component(componentName, edges1 ++ edges2)
      }
    )

  }


}
