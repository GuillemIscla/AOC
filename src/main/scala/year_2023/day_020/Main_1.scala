package year_2023.day_020

object Main_1 extends App {
  val moduleSet = Parser.readInput(isSample = None)

  val printPulses = true
  val iterations = 1

  println((0 until iterations).foldLeft(PulsesCount(0,0)){
    case (accPulses, i) =>
      if(printPulses) println(s"$i: -------------")
      accPulses.add(moduleSet.pulseButton.sendPulse(printPulses))
  }.value)

}
