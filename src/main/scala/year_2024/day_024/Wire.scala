package year_2024.day_024

case class Wire(name:String, value:Option[Boolean])

trait GateType {
  def operate(a:Boolean, b:Boolean):Boolean
}

object GateType {
  def fromRaw(raw:String):GateType = all.find(_.toString == raw).get

  def all:List[GateType] = List(AND, OR, XOR)
}

case object AND extends GateType {
  override def toString: String = "AND"
  override def operate(a: Boolean, b: Boolean): Boolean = a && b
}
case object OR extends GateType {
  override def toString: String = "OR"
  override def operate(a: Boolean, b: Boolean): Boolean = a || b
}
case object XOR extends GateType {
  override def toString: String = "XOR"
  override def operate(a: Boolean, b: Boolean): Boolean = a != b
}
case object False extends GateType {
  override def toString: String = "False"
  override def operate(a: Boolean, b: Boolean): Boolean = false
}





case class Gate(gateType: GateType, aName:String, bName:String, cName:String){
  override def toString: String = s"$aName $gateType $bName = $cName"
  def inputs:Set[String] = Set(aName, bName)
}

case class SwapManager(swaps:Map[String, String]){
  def addSwaps(newSwaps:List[(String, String)]):SwapManager = {
    SwapManager(swaps ++ newSwaps.flatMap {
      case (a, b) => List((a, b), (b, a))
    }.distinct)
  }
  def printSwaps:String = swaps.flatMap {
    case (a, b) => List(a, b)
  }.toList.distinct.sorted.mkString(",")
  def swapWire(wire:String):String = swaps.getOrElse(wire, wire)
}


case class WireSystem(wires:List[Wire]){

  def initialize(initialValues:Map[String, Boolean]):WireSystem =
    WireSystem(
      wires.map{
        case Wire(name, _) => Wire(name, initialValues.get(name))
      }
    )


  def operate(gate:Gate):WireSystem = {
    val wireA = wires.find(_.name == gate.aName).get
    val wireB = wires.find(_.name == gate.bName).get
    val wireC = wires.find(_.name == gate.cName).get
    (wireA.value, wireB.value, wireC.value) match {
      case (Some(wireAValue), Some(wireBValue), None) =>
        val newWireC = Wire(gate.cName, Some(gate.gateType.operate(wireAValue, wireBValue)))
        WireSystem(newWireC :: wires.filterNot(_.name == gate.cName))
      case _ =>
        this
    }
  }

  def getVarAsWires(prefix:String):List[Wire] =
    wires
      .filter(wire => wire.name.startsWith(prefix) && wire.name.drop(1).toIntOption.isDefined)
      .sortBy(_.name)

  def getVarAsBase2(prefix:String):List[Boolean] = getVarAsWires(prefix).flatMap(_.value)

  def getVarAsLong(prefix:String):Long = {
    val base2 = getVarAsBase2(prefix)
    var accInt: Long = 0L
    var i = 0
    var pow = 1L
    while (base2.size > i) {
      accInt += {
        if (base2(i)) pow else 0L
      }
      i += 1
      pow *= 2
    }
    accInt
  }

}