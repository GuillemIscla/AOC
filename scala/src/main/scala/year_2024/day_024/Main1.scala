package year_2024.day_024

object Main1 extends App {

  val (initialWireSystem, initialValues, gates) = Parser.readInput(isSample = false)

  def process(wireSystem: WireSystem, initialValues: Map[String, Boolean], gates: List[Gate]): WireSystem = {
    var i = 0
    var currentWireSystem = wireSystem.initialize(initialValues)
    while (!currentWireSystem.getVarAsWires("z").forall(_.value.isDefined)) {
      currentWireSystem = currentWireSystem.operate(gates(i))
      i = (i + 1) % gates.size
    }
    currentWireSystem
  }

  println(process(initialWireSystem, initialValues, gates).getVarAsLong("z"))


}
