package year_2024.day_007

case class Equation(result:Long, operands:List[Long])

trait Operator {
  def operate(a:Long, b:Long): Long
}

case object Mult extends Operator {
  override def operate(a: Long, b: Long): Long = a * b
}

case object Sum extends Operator {
  override def operate(a: Long, b: Long): Long = a + b
}

case object Or extends Operator {
  override def operate(a: Long, b: Long): Long = (a.toString + b.toString).toLong
}

object Operator {
  def generate(size:Long, acc:List[List[Operator]] = List(List(Sum), List(Mult))):List[List[Operator]] = {
    if(size == 1) acc
    else {
      generate(size - 1, acc.flatMap{
        innerList => List(
          Mult :: innerList,
          Sum :: innerList
        )
      })
    }
  }

  def generateWithOr(size: Long, acc: List[List[Operator]] = List(List(Sum), List(Mult), List(Or))): List[List[Operator]] = {
    if (size == 1) acc
    else {
      generateWithOr(size - 1, acc.flatMap {
        innerList =>
          List(
            Mult :: innerList,
            Sum :: innerList,
            Or :: innerList
          )
      })
    }
  }
}