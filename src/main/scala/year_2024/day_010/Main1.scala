package year_2024.day_010

object Main1 extends App {

  val input = Parser.readInput(isSample = None)

  val trailHeads = input.getTrailHeads

  val trailHeadsWithTrails =
    trailHeads.map{
      trailHead =>
        val trails =
          (1 to 9).foldLeft(List(List(trailHead))){
            case (trails, n) =>
              trails.flatMap{
                case trail :+ last =>
                  input.getNextStep(last, n).map{
                    nextStep => trail :+ last :+ nextStep
                  }

              }.groupBy(_.last).values.map(_.head).toList
          }
        (trailHead, trails)
    }

  println(trailHeadsWithTrails.map(_._2.size).sum)

}
