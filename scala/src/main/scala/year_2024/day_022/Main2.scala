package year_2024.day_022

import year_2024.day_022.Secret.{allSteps, getIterator}


object Main2 extends App {

  val initialSecrets = Parser.readInput(isSample = false)

  val counter:Map[List[Int], (Int, Set[Int])] = Map.empty

  val aggregatedCounter =
    (0 until 2000).foldLeft((counter, initialSecrets.map(getIterator))){
      case ((counterAcc, secretsIterating), _) if secretsIterating.head.prices.size < 5 =>
        (counterAcc, secretsIterating.map(_.iterate()))
      case ((counterAcc, secretsIterating), _) =>
        val newSecretsIterating = secretsIterating.map(_.iterate())
        val newCounterAcc =
          secretsIterating.zipWithIndex.foldLeft(counterAcc){
            case (counterAccAcc, (newSecretIterating, index)) =>
              val pattern = newSecretIterating.getPattern
              counterAccAcc.get(pattern) match {
                case Some((sum, participated)) if !participated.contains(index) =>
                  counterAccAcc + (pattern -> (sum + newSecretIterating.prices.last, participated + index))
                case Some(_) =>
                  counterAccAcc
                case None =>
                  counterAccAcc + (pattern -> (newSecretIterating.prices.last, Set(index)))
              }
          }
        (newCounterAcc, newSecretsIterating)
    }._1

  println(aggregatedCounter.toList.map{
    case (_, (sum, _)) => sum
  }.max)

}
