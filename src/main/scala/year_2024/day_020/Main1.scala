package year_2024.day_020

object Main1 extends App {

  val race = Parser.readInput(isSample = false)


  println(race.path.flatMap(race.savesOnCheat).count(_._3 >= 100))


  def printMap(trace:List[Position]):Unit = {
    race.raw.zipWithIndex.foreach{
      case (row, i) =>
        row.zipWithIndex.foreach{
          case (char, j) =>
            if (trace.contains(Position(i,j))) print('O')
            else print(char)
        }
        println()
    }
  }

}
