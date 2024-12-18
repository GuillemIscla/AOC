package year_2024.day_017

import scala.annotation.tailrec
import scala.util._

case class Registers(registerA:Long, registerB:Long, registerC: Long)

case class ThreeBitProgram(registers: Registers, intBand:List[Int]){
  def execute():(Registers, List[Long]) = executeInternal(pointer = 0, registers, List.empty)

  @tailrec
  private def executeInternal(pointer:Int, accRegisters:Registers, output:List[Long]):(Registers, List[Long]) = {
    read(pointer) match {
      case Success((opcode, operand)) =>
        val OpResult(opRegisters, opOutput, opJump) = Instruction.build(opcode, operand).operate(accRegisters)
        val newPointer = opJump match {
          case Some(jump) => jump
          case _ => pointer + 2
        }
        val newOutput = output ++ opOutput
        executeInternal(newPointer, opRegisters, newOutput)

      case _ => (accRegisters, output)
    }
  }

  private def read(pointer:Int):Try[(Int, Int)] =
    for {
      opcode <- Try(intBand(pointer))
      operand <- Try(intBand(pointer + 1))
    } yield (opcode, operand)
}
