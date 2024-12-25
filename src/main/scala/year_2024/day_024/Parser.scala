package year_2024.day_024

import scala.io.Source

object Parser {

  def readInput(isSample:Boolean):(WireSystem, Map[String, Boolean], List[Gate])  = {
    if(isSample){
      parseInput("""x00: 1
                   |x01: 0
                   |x02: 1
                   |x03: 1
                   |x04: 0
                   |y00: 1
                   |y01: 1
                   |y02: 1
                   |y03: 1
                   |y04: 1
                   |
                   |ntg XOR fgs -> mjb
                   |y02 OR x01 -> tnw
                   |kwq OR kpj -> z05
                   |x00 OR x03 -> fst
                   |tgd XOR rvg -> z01
                   |vdt OR tnw -> bfw
                   |bfw AND frj -> z10
                   |ffh OR nrd -> bqk
                   |y00 AND y03 -> djm
                   |y03 OR y00 -> psh
                   |bqk OR frj -> z08
                   |tnw OR fst -> frj
                   |gnj AND tgd -> z11
                   |bfw XOR mjb -> z00
                   |x03 OR x00 -> vdt
                   |gnj AND wpb -> z02
                   |x04 AND y00 -> kjc
                   |djm OR pbm -> qhw
                   |nrd AND vdt -> hwm
                   |kjc AND fst -> rvg
                   |y04 OR y02 -> fgs
                   |y01 AND x02 -> pbm
                   |ntg OR kjc -> kwq
                   |psh XOR fgs -> tgd
                   |qhw XOR tgd -> z09
                   |pbm OR djm -> kpj
                   |x03 XOR y03 -> ffh
                   |x00 XOR y04 -> ntg
                   |bfw OR bqk -> z06
                   |nrd XOR fgs -> wpb
                   |frj XOR qhw -> z04
                   |bqk OR frj -> z07
                   |y03 OR x01 -> nrd
                   |hwm AND bqk -> z03
                   |tgd XOR rvg -> z12
                   |tnw OR pbm -> gnj""".stripMargin.split("\n").toList)
    }
    else {
      parseInput(Source.fromResource("year_2024/day_024_input.txt").getLines().toList)
    }
  }

  def parseInput(lines: List[String]):(WireSystem, Map[String, Boolean], List[Gate]) = {
    val separation = lines.indexWhere(_.isEmpty)
    val initialValuesRaw = lines.take(separation)
    val initialValuesRegex = "([A-z,0-9]{3}): ([0,1])".r
    val initialValues:Map[String, Boolean] = initialValuesRaw.map{
      initialWire =>
        val initialWireMatch = initialValuesRegex.findFirstMatchIn(initialWire).get
        initialWireMatch.group(1) -> (initialWireMatch.group(2).toInt == 1)
    }.toMap

    val gatesRegex = "([A-z,0-9]{3}) ([A-Z]+) ([A-z,0-9]{3}) -> ([A-z,0-9]{3})".r
    val gatesRaw = lines.drop(separation + 1)
    val gates:List[Gate] =
      gatesRaw.map{
        gateRaw =>
          val gateMatch = gatesRegex.findFirstMatchIn(gateRaw).get
          Gate(
            gateType = GateType.fromRaw(gateMatch.group(2)),
            aName = gateMatch.group(1),
            bName = gateMatch.group(3),
            cName = gateMatch.group(4)
          )
      }
    val wires = gates.flatMap{
      case Gate(_, aName, bName, cName) =>
        List(aName, bName, cName)
    }.distinct.map{
      wireName =>
        Wire(wireName, None)
    }

    (WireSystem(wires), initialValues,  gates)
  }



}
