package year_2023.day_020

import year_2023.helper.ListUtils.ListSyntax

trait Pulse

case object High extends Pulse {
  override def toString: String = "-high->"
}

case object Low extends Pulse{
  override def toString: String = "-low->"
}

trait Module {
  def symbol:String
  def name:String
  override def toString: String = s"$symbol$name -> ${receivers.map(_.name).mkString(", ")}"
  def receivers:List[Module]
  def addReceivers(receivers:List[Module]):Unit
  def stateChange(maybeModule:Option[Module], pulse: Pulse):Unit
  def processPulse(pulse:Pulse):List[PulseSent]
  def receivePulse(maybeModule: Option[Module], pulse: Pulse, printPulses: Boolean): () => List[PulseSent] = {
    () => {
      stateChange(maybeModule, pulse)
      val pulsesSent = processPulse(pulse)
      if (printPulses) pulsesSent.foreach(pulseSent => println(s"$name ${pulseSent.pulse} ${pulseSent.sent.name}"))
      pulsesSent
    }
  }

  def reset():Unit

}

case class PulseSent(sender:Module, sent:Module, pulse: Pulse)

trait OnOff
case object On extends OnOff
case object Off extends OnOff

case class FlipFlop(name:String) extends Module {
  val symbol:String = "%"
  var receivers:List[Module] = List.empty

  var state:OnOff = Off
  override def addReceivers(receivers: List[Module]): Unit = this.receivers = receivers
  override def stateChange(maybeModule:Option[Module], pulse: Pulse): Unit = (state, pulse) match {
    case (Off, Low) => state = On
    case (On, Low) => state = Off
    case _ =>
  }
  override def processPulse(pulse: Pulse): List[PulseSent] = (state, pulse) match {
    case (On, Low) => //Already modified state previously to run this
      receivers.map(r => PulseSent(this, r, High))
    case (Off, Low) => //Already modified state previously to run this
      receivers.map(r => PulseSent(this, r, Low))
    case _ =>
      List.empty
  }

  override def reset(): Unit = state = Off
}

case class Conjunction(name:String) extends Module {
  val symbol:String = "&"
  var state:List[(String, Pulse)] = List.empty
  var receivers:List[Module] = List.empty
  def addInputs(modules:List[Module]):Unit =
    state = modules.map(module => (module.name, Low))
  override def addReceivers(receivers: List[Module]): Unit = this.receivers = receivers
  override def stateChange(maybeModule:Option[Module], pulse: Pulse): Unit = {
    val module = maybeModule.getOrElse(throw new Exception("Conjunction module received pulse from nowhere"))
    state = state.replaceWhere(_._1 == module.name, (module.name, pulse))
  }

  override def processPulse(pulse: Pulse): List[PulseSent] = {
    if (state.forall(_._2 == High)) {
      receivers.map(r => PulseSent(this, r, Low))
    }
    else {
      receivers.map(r => PulseSent(this, r, High))
    }
  }

  override def reset(): Unit = state = state.map{case (moduleName, _) => (moduleName, Low)}
}

case class Broadcaster() extends Module {
  val symbol:String = ""
  val name:String = "broadcaster"
  var receivers:List[Module] = List.empty
  override def addReceivers(receivers: List[Module]): Unit = this.receivers = receivers
  override def stateChange(maybeModule: Option[Module], pulse: Pulse): Unit = ()
  override def processPulse(pulse: Pulse): List[PulseSent] = receivers.map(r => PulseSent(this, r, pulse))

  override def reset(): Unit = ()

}

case class Output(name:String) extends Module {
  val symbol: String = ""
  val receivers: List[Module] = List.empty

  var lowPulses:Int = 0
  override def addReceivers(receivers: List[Module]): Unit = ()
  override def stateChange(maybeModule: Option[Module], pulse: Pulse): Unit = ()
  override def processPulse(pulse: Pulse): List[PulseSent] = {
    lowPulses += 0
    List.empty
  }

  override def reset(): Unit = ()
}
object Module {
  def fromRaw(raw:String):Module = raw match {
    case flipFlop if flipFlop.head == '%' => FlipFlop(raw.tail)
    case conjunction if conjunction.head == '&' => Conjunction(raw.tail)
    case "broadcaster" => Broadcaster()
    case _ => throw new Exception(s"Could not parse a Module with raw: '$raw'")
  }
}

case class PulsesCount(low:Long, high:Long){
  def add(other:PulsesCount):PulsesCount = {
    PulsesCount(
      low = low + other.low,
      high = high + other.high
    )
  }

  val value:Long = low * high
}

object PulsesCount {
  def apply(pulses:List[PulseSent]): PulsesCount = {
    PulsesCount(low = pulses.count(_.pulse == Low), high = pulses.count(_.pulse == High))
  }
}

case class PulseButton(broadcaster: Broadcaster) {
  def sendPulse(printPulses:Boolean):PulsesCount = {
    if(printPulses) println("button -low-> broadcaster")
    var pulsesToProcess = List(broadcaster.receivePulse(None, Low, printPulses))
    var pulsesCount = PulsesCount(low = 1, high = 0)
    while(pulsesToProcess.nonEmpty){
      val nextToProcess = pulsesToProcess.head
      val nextSend = nextToProcess()
      val nextPulsesToProcess = nextSend.map{ case PulseSent(sender, sent, pulse) => sent.receivePulse(Some(sender), pulse, printPulses) }
      pulsesToProcess = pulsesToProcess.tail ++ nextPulsesToProcess
      pulsesCount = pulsesCount.add(PulsesCount(nextSend))
    }
    pulsesCount

  }
}


case class ModuleSet(modulesAndReceivers:List[(Module, List[String])]){
  override def toString: String = modules.mkString("\n")

  val modules: List[Module] = modulesAndReceivers.map(_._1)

  modulesAndReceivers.foreach {
    case (module, receiverNames) =>
      val receivers = receiverNames.flatMap(moduleName => modules.find(_.name == moduleName))
      module.addReceivers(receivers)
  }

  private val conjunctions = modules.collect{ case conjunction: Conjunction => conjunction }
  conjunctions.foreach {
    conjunction =>
      conjunction.addInputs(modules.filter(_.receivers.exists(m => m.name == conjunction.name)))
  }

  val pulseButton:PulseButton = PulseButton(
    modules
      .collectFirst {
        case broadcaster: Broadcaster => broadcaster
      }
      .getOrElse(throw new Exception("Broadcaster not found"))
  )

  def reset():Unit = modules.foreach(_.reset())

}