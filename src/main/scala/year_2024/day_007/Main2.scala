package year_2024.day_007


object Main2 extends App {
  val input = Parser.readInput(isSample = false)

  println(input.filter {
    equation =>
      val possibleOperators = Operator.generateWithOr(equation.operands.size - 1)
      val possibleResults =
        possibleOperators.map {
          operator =>
            operator.zip(equation.operands.tail).foldLeft(equation.operands.head) {
              case (-1, _) => -1
              case (acc, (operator, newOperand)) =>
                val value = operator.operate(acc, newOperand)
                if (value > equation.result) -1
                else value
            }
        }
      possibleResults.contains(equation.result)
  }.map(_.result).sum)

}
