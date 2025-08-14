package year_2024.day_003


sealed trait Instruction
case class Mul(a:Int, b:Int) extends Instruction{
  def result:Int = a * b

}

case class DoOrDont(isDo:Boolean) extends Instruction