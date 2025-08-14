package year_2024.day_002


//274
//298
object Main2 extends App {
  val input = Parser.readInput(isSample = false)

  println(input.count {
    report =>
      analyze(report) match {
        case Some(problemLevel) =>
          analyze(dumpLevel(report, problemLevel)).isEmpty ||
            analyze(dumpLevel(report, problemLevel + 1)).isEmpty ||
            analyze(dumpLevel(report, 0)).isEmpty

        case None =>
          true
      }
  })

  def analyze(report:List[Int]):Option[Int] = {
    report.zip(report.head :: report) match {
      case _ :: middle =>
        val minusList = middle.map { case (a, b) => b - a }
        if (minusList.contains(0))
          minusList.zipWithIndex.find{ case (elem, _) => elem == 0 }.map(_._2)
        else if (minusList.head < 0)
          minusList.zipWithIndex.find { case (elem, _) => elem > 0 || elem <= -4 }.map(_._2)
        else
          minusList.zipWithIndex.find { case (elem, _) => elem < 0 || elem >= 4 }.map(_._2)
    }
  }
  def dumpLevel(report:List[Int], problem:Int):List[Int] = {
    report.take(problem) ++ report.takeRight(report.size - problem - 1)
  }

}
