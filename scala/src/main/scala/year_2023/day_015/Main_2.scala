package year_2023.day_015

object Main_2 extends App {
  val input = Parser.readInput(isSample = false)
  val regex = "([a-z]+)(\\-|=[0-9]+)".r

  val instructions = input.map(ASCII_Transformer.transformInstruction)
  val processedBox = instructions.foldLeft(LensBoxLine.empty()){
    case (accLensBoxLine, newInstruction) => accLensBoxLine.processInstruction(newInstruction)
  }
  println(processedBox.boxes.map(_.focusingPower).sum)
}
