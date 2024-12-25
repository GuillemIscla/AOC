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
      val (bIWire, cIWire) = bWireCWire(i, bIGate, cIGate, swapManagerAcc)
      val dIGate = dGate(i, bIWire)
      val (aIWire, dIWire) = aWireDWire(i, aIGate, dIGate, swapManagerAcc)
      val zIGate = zGate(i, bIWire)
      val zIWire = swapManagerAcc.swapWire(zWire(i))
      val newSwaps = List(
        (aIGate.cName, aIWire),
        (bIGate.cName, bIWire),
        (cIGate.cName, cIWire),
        (dIGate.cName, dIWire),
        (zIGate.cName, zIWire)
      ).filter{
        case (a, b) => a != b
      }
      

      (aWires :+ aIWire, bWires :+ bIWire, cWires :+ cIWire, dWires :+ dIWire, swapManagerAcc.addSwaps(newSwaps))
  }

  println(swapManager.swaps.size)
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
  def bWireCWire(i: Int, bIGate:Gate, cIGate:Gate, swapManager: SwapManager): (String, String) = {
    val bWire = swapManager.swapWire(bIGate.cName)
    val cWire = swapManager.swapWire(cIGate.cName)
    val bIsReal =
      initialGates.exists(g => g.inputs.contains(bWire) && g.gateType == AND) &&
        initialGates.exists(g => g.inputs.contains(bWire) && g.gateType == XOR)
    val cIsReal =
      initialGates.exists(g => g.inputs.contains(cWire) && g.gateType == AND) &&
        initialGates.exists(g => g.inputs.contains(cWire) && g.gateType == XOR)
    if(bIsReal && cIsReal) (bWire, cWire)
    else if(bIsReal){
      val zGate = lookForGate("bWireCWire", i)(g => g.inputs.contains(bWire) && g.gateType == XOR)
      (bWire, zGate.inputs.find(_ != bWire).get)
    }
    else {
      val zGate = lookForGate("bWireCWire", i)(g => g.inputs.contains(cWire) && g.gateType == XOR)
      (zGate.inputs.find(_ != cWire).get, cWire)
    }
  }
  def dGate(i:Int, bIWire:String): Gate = {
    lookForGate("dGate", i)(g =>
      g.inputs.contains(bIWire) && g.gateType == AND
    )
  }
  def aWireDWire(i:Int, aGate:Gate, dGate:Gate, swapManager: SwapManager): (String, String) = {
    val aWire = swapManager.swapWire(aGate.cName)
    val dWire = swapManager.swapWire(dGate.cName)
    val bothReal = initialGates.exists(g =>
      g.inputs == Set(aWire, dWire) && g.gateType == OR
    )
    if(bothReal) (aWire, dWire)
    else {
      initialGates.find(g =>
        g.inputs.contains(dWire) && g.gateType == OR
      ) match {
        case Some(cPlus1Gate) =>
          (cPlus1Gate.inputs.find(_ != dWire).get, dWire)
        case None =>
          (aWire, initialGates.find(g =>
            g.inputs.contains(aWire) && g.gateType == OR
          ).get.inputs.find(_ != aWire).get)
      }
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
