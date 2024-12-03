package year_2024.day_003

object Main2 extends App {
  val input = Parser.readInput(isSample = false)

  val filterMuls = input.foldLeft((List.empty[Mul], true)){
    case ((accList, true), mul:Mul) => (accList :+ mul, true)
    case ((accList, false), _: Mul) => (accList, false)
    case ((accList, _), DoOrDont(true)) => (accList, true)
    case ((accList, _), DoOrDont(false)) => (accList, false)
  }._1

  println(filterMuls.map(_.result).sum)


}
