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
    val parsedLines = lines.map{
      line =>
        val result = wiredRegex.findFirstMatchIn(line).getOrElse(throw new Exception(s"Could not parse line '$line'"))

        (ComponentBuilder(result.group(1)), result.group(2).tail.split(" ").toList.map(ComponentBuilder.apply))
    }

    val components = parsedLines.flatMap{ case (right, leftList) => right :: leftList }.distinct
    parsedLines.foreach{
      case (right, leftList) =>
        val rightC = components.find(_.name == right.name).get
        val leftC = leftList.flatMap(c => components.find(_.name == c.name))
        leftC.foreach{
          c =>
            c.addComponent(rightC.name)
            rightC.addComponent(c.name)
        }
    }
    Apparatus(components.map(_.toComponent))
  }


}
