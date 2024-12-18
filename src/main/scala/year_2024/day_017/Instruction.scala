package year_2024.day_017

import year_2024.day_017.Instruction._


trait Instruction{
  def opcode:Int
  def operand:Long
  def operate(registers: Registers):OpResult
}

case class OpResult(registers: Registers, output: Option[Long], jump:Option[Int])

object Instruction {
  def renderCombo(registers: Registers, comboOperand:Long):Long = {
    comboOperand match {
      case 0 | 1 | 2 | 3 => comboOperand
      case 4 => registers.registerA
      case 5 => registers.registerB
      case 6 => registers.registerC
      case 7 => throw new Exception("Combo operand 7 is reserved and will not appear in valid programs.")
    }
  }

  def build(opcode:Int, operand:Long):Instruction = {
    opcode match {
      case 0 => Adv(operand)
      case 1 => Bxl(operand)
      case 2 => Bst(operand)
      case 3 => Jnz(operand)
      case 4 => Bxc(operand)
      case 5 => Out(operand)
      case 6 => Bdv(operand)
      case 7 => Cdv(operand)
    }
  }

  def xor(a:BigInt, b:BigInt):Long = {
    val aCoded = toBase2(a)
    val bCoded = toBase2(b)
    val aPadded = List.fill(bCoded.size - aCoded.size)(false) ++ aCoded
    val bPadded = List.fill(aCoded.size - bCoded.size)(false) ++ bCoded
    fromBase2(aPadded.zip(bPadded).map{
      case (true, true) => false
      case (false, false) => false
      case _ => true
    })
  }

  def toBase2(int:BigInt):List[Boolean] = {
    var accInt = int
    var result = List.empty[Boolean]
    while(accInt > 0) {
      result = (accInt % 2 == 1) :: result
      accInt /= 2
    }
    result
  }
  def fromBase2(base2:List[Boolean]):Long = {
    var accInt:Long = 0L
    var i = 0
    var pow = 1L
    while (base2.size > i) {
      accInt += { if(base2.reverse(i)) pow else 0L }
      i += 1
      pow *= 2
    }
    accInt
  }
}

/**
 *
 *
 * The adv instruction (opcode 0) performs division. The numerator is the value in the A register. The denominator is found by raising 2 to the power of the instruction's combo operand. (So, an operand of 2 would divide A by 4 (2^2); an operand of 5 would divide A by 2^B.) The result of the division operation is truncated to an integer and then written to the A register.
 * The bxl instruction (opcode 1) calculates the bitwise XOR of register B and the instruction's literal operand, then stores the result in register B.
 * The bst instruction (opcode 2) calculates the value of its combo operand modulo 8 (thereby keeping only its lowest 3 bits), then writes that value to the B register.
 * The jnz instruction (opcode 3) does nothing if the A register is 0. However, if the A register is not zero, it jumps by setting the instruction pointer to the value of its literal operand; if this instruction jumps, the instruction pointer is not increased by 2 after this instruction.
 * The bxc instruction (opcode 4) calculates the bitwise XOR of register B and register C, then stores the result in register B. (For legacy reasons, this instruction reads an operand but ignores it.)
 * The out instruction (opcode 5) calculates the value of its combo operand modulo 8, then outputs that value. (If a program outputs multiple values, they are separated by commas.)
 * The bdv instruction (opcode 6) works exactly like the adv instruction except that the result is stored in the B register. (The numerator is still read from the A register.)
 * The cdv instruction (opcode 7) works exactly like the adv instruction except that the result is stored in the C register. (The numerator is still read from the A register.)
 *
 * */

case class Adv(operand: Long) extends Instruction {
  override def opcode: Int = 0
  override def operate(registers: Registers): OpResult = {
    val newRegisterA = registers.registerA / Math.pow(2, renderCombo(registers, operand)).toInt
    OpResult(registers.copy(registerA = newRegisterA), None, None)
  }
}
case class Bxl(operand: Long) extends Instruction {
  override def opcode: Int = 1
  override def operate(registers: Registers): OpResult = {
    val newRegisterB = xor(registers.registerB, operand)
    OpResult(registers.copy(registerB = newRegisterB), None, None)
  }
}
case class Bst(operand: Long) extends Instruction {
  override def opcode: Int = 2
  override def operate(registers: Registers): OpResult = {
    val newRegisterB = renderCombo(registers, operand) % 8
    OpResult(registers.copy(registerB = newRegisterB), None, None)
  }
}
case class Jnz(operand: Long) extends Instruction {
  override def opcode: Int = 3
  override def operate(registers: Registers): OpResult = {
    val registerA = registers.registerA
    if(registerA != 0) OpResult(registers, None, Some(operand.toInt))
    else OpResult(registers, None, None)
  }
}
case class Bxc(operand: Long) extends Instruction {
  override def opcode: Int = 4
  override def operate(registers: Registers): OpResult = {
    val newRegisterB = xor(registers.registerB, registers.registerC)
    OpResult(registers.copy(registerB = newRegisterB), None, None)
  }
}
case class Out(operand: Long) extends Instruction {
  override def opcode: Int = 5
  override def operate(registers: Registers): OpResult = {
    val out = renderCombo(registers, operand) % 8
    OpResult(registers, Some(out), None)
  }
}
case class Bdv(operand: Long) extends Instruction {
  override def opcode: Int = 6
  override def operate(registers: Registers): OpResult = {
    val newRegisterB = registers.registerA / Math.pow(2, renderCombo(registers, operand)).toInt
    OpResult(registers.copy(registerB = newRegisterB), None, None)
  }
}
case class Cdv(operand: Long) extends Instruction {
  override def opcode: Int = 7
  override def operate(registers: Registers): OpResult = {
    val newRegisterC = registers.registerA / Math.pow(2, renderCombo(registers, operand)).toInt
    OpResult(registers.copy(registerC = newRegisterC), None, None)
  }
}


