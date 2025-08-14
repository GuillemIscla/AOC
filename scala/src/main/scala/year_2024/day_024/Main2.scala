package year_2024.day_024

object Main2 extends App {


  val (initialWireSystem, _, initialGates) = Parser.readInput(isSample = false)

  /**
   *
   * zi=(xi⊕yi)⊕ci
   * c0=false
   * ci+1=(xi∧yi)∨(ci∧(xi⊕yi))
   * We will name:
   * ai=xi∧yi
   * bi=xi⊕yi
   * di=ci∧bi
   * zi=bi⊕ci
   * And:
   * ci+1=ai∨di
   *
   * At i:
   * a: gi (no param)
   * b: gi (no param)
   * c: gi (from a-1 d-1)
   * z:
   * d:
   * z0=x0⊕y0
   * z1=b1⊕n
   * m=x1⊕y1 <-- b1 gate
   * n=x0∧y0  <--a0 gate
   *
   *
   * */

  val initialAWires = List(aGate(0).cName, aGate(1).cName)
  val initialBWires = List(bGate(0).cName, bGate(1).cName)
  val initialCWires = List[String](null, null)
  val initialDWires = List[String](null, null)

  val (_, _, _, _, swapManager) = (2 until 45).foldLeft((initialAWires, initialBWires, initialCWires, initialDWires, SwapManager(Map.empty))) {
    case ((aWires, bWires, cWires, dWires, swapManagerAcc), i) =>
      val aIGate = aGate(i)
      val bIGate = bGate(i)
      val cIGate = cGate(i, aWires(i-1))
      val zIWire = zWire(i)
      val (bIWire, cIWire) = bWireCWire(i, zIWire, bIGate, cIGate, swapManagerAcc)
      val dIGate = dGate(i, bIWire)
      val (aIWire, dIWire) = aWireDWire(i, aIGate, dIGate, swapManagerAcc)
      val zIGate = zGate(i, bIWire)
      val newSwaps = List(
        (aIGate.cName, aIWire),
        (bIGate.cName, bIWire),
        (cIGate.cName, cIWire),
        (dIGate.cName, dIWire),
        (zIGate.cName, zIWire)
      ).filter {
        case (a, b) => a != b
      }

      (aWires :+ aIWire, bWires :+ bIWire, cWires :+ cIWire, dWires :+ dIWire, swapManagerAcc.addSwaps(newSwaps))
  }

  println(swapManager.printSwaps)

  def aGate(i:Int):Gate = {
    lookForGate("aGate", i)(g =>
      g.inputs == Set(toNodeName("x", i), toNodeName("y", i)) &&
        g.gateType == AND &&
        g.cName != "z00"
    )
  }
  def bGate(i: Int): Gate = {
    lookForGate("bGate", i)(g =>
        g.inputs == Set(toNodeName("x", i), toNodeName("y", i)) &&
          g.gateType == XOR
    )
  }
  def bWireCWire(i: Int, zWire:String, bIGate:Gate, cIGate:Gate, swapManager: SwapManager): (String, String) = {
    val (bWire, bWasSwapped) = swapManager.swapWire(bIGate.cName)
    val (cWire, cWasSwapped) = swapManager.swapWire(cIGate.cName)
    if(bWasSwapped || cWasSwapped) (bWire, cWire)
    else {
      val inputs = lookForGate("bWireCWirePossibilities", i){
        g => g.gateType == XOR && Set(bWire, cWire, zWire).intersect(Set(g.aName, g.bName, g.cName)).size > 1
      }.inputs
      if(inputs.contains(bWire))
        (bWire, inputs.find(_ != bWire).get)
      else
        (inputs.find(_ != cWire).get, cWire)
    }
  }
  def dGate(i:Int, bIWire:String): Gate = {
    lookForGate("dGate", i)(g =>
      g.inputs.contains(bIWire) && g.gateType == AND
    )
  }
  def aWireDWire(i:Int, aGate:Gate, dGate:Gate, swapManager: SwapManager): (String, String) = {
    val (aWire, aWasSwapped) = swapManager.swapWire(aGate.cName)
    val (dWire, dWasSwapped) = swapManager.swapWire(dGate.cName)

    if(aWasSwapped || dWasSwapped) (aWire, dWire)
    else {
      val inputs = lookForGate("aWireDWirePossibilities", i) {
        g => g.gateType == OR && Set(aWire, dWire).intersect(g.inputs).nonEmpty
      }.inputs
      if (inputs.contains(aWire))
        (aWire, inputs.find(_ != aWire).get)
      else
        (inputs.find(_ != dWire).get, dWire)
    }
  }
  def cGate(i:Int, aI1Wire:String): Gate  = {
     lookForGate("cGate", i)(g =>
        g.inputs.contains(aI1Wire) &&
          g.gateType == OR
      )
  }
  def zGate(i:Int, bIWire:String): Gate = {
    lookForGate("zGate", i)(g => g.inputs.contains(bIWire) && g.gateType == XOR)
  }
  def zWire(i:Int):String = toNodeName("z", i)


  def toNodeName(prefix:String, number:Int):String =
    if(number < 10) s"${prefix}0$number" else s"$prefix$number"

  def lookForGate(function:String, i:Int)(condition:Gate => Boolean):Gate = {
    initialGates.filter(condition) match {
      case head :: Nil => head
      case other => throw new Exception(s"Got ${other.size} candidates for $function $i")
    }
  }



}
