package year_2024.day_023

import scala.io.Source

object Parser {

  def readInput(isSample:Boolean):(List[Computer], List[ComputerLink])  = {
    if(isSample){
      parseInput("""kh-tc
                   |qp-kh
                   |de-cg
                   |ka-co
                   |yn-aq
                   |qp-ub
                   |cg-tb
                   |vc-aq
                   |tb-ka
                   |wh-tc
                   |yn-cg
                   |kh-ub
                   |ta-co
                   |de-co
                   |tc-td
                   |tb-wq
                   |wh-td
                   |ta-ka
                   |td-qp
                   |aq-cg
                   |wq-ub
                   |ub-vc
                   |de-ta
                   |wq-aq
                   |wq-vc
                   |wh-yn
                   |ka-de
                   |kh-ta
                   |co-tc
                   |wh-qp
                   |tb-vc
                   |td-yn
                   |""".stripMargin.split("\n").toList)
    }
    else {
      parseInput(Source.fromResource("year_2024/day_023_input.txt").getLines().toList)
    }
  }

  def parseInput(lines: List[String]):(List[Computer], List[ComputerLink]) = {
    lines.foldLeft((List.empty[Computer], List.empty[ComputerLink])) {
      case ((accComputers, accLinks), newLine) =>
        val Array(a, b) = newLine.split("-")
        ((Computer(a) :: Computer(b) :: accComputers).distinct, ComputerLink(a, b) :: accLinks)
    }
  }



}
