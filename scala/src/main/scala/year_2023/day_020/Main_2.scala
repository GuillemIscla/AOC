package year_2023.day_020


/**
 * Looking at the input one realizes that there are 4 flip flop chains that lead to a much smaller graph of conjunctions
 * Also this flip flop chains get short-circuited and reset all to Off each one at a certain point by a conjunction
 * When reset, is the only moment when the conjunction sends a Low signal. You just need to find those resets and multiply
 * them
 * */
object Main_2 extends App {
  val moduleSet = Parser.readInput(isSample = None)
  def getBroadcaster(): Broadcaster = {
    moduleSet.modules.collectFirst {
      case broadcaster:Broadcaster => broadcaster
    }.getOrElse(throw new Exception(s"Broadcaster not found"))
  }

  def getFlipFlopChain(origin: FlipFlop):List[FlipFlop] = {
    origin.receivers.collectFirst{ case ff:FlipFlop => ff } match {
      case Some(next) => origin :: getFlipFlopChain(next)
      case None => origin :: Nil
    }
  }

  def calculateReset(flipFlopChain: List[FlipFlop]): Long = {
    moduleSet.reset()
    var i = 0
    var finished = false
    while (!finished) {
      moduleSet.pulseButton.sendPulse(printPulses = false)
      if (flipFlopChain.forall(_.state == Off)) finished = true
      i += 1
    }
    i
  }

  val roots = getBroadcaster().receivers.collect{ case ff:FlipFlop => ff }
  println(roots.map(getFlipFlopChain).map(calculateReset).product)


}
