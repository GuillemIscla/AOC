package year_2023.day_003

import scala.util.Try


case class NumberInEngine(value:Int, rowIndex:Int, start:Int, end:Int){
  def adjacents(): List[(Int, Int)] =
    (start - 1 to end + 1).map(column => (rowIndex - 1, column)).toList ++
      (start - 1 to end + 1).map(column => (rowIndex + 1, column)).toList ++
      List((rowIndex, start - 1), (rowIndex, end + 1))
}
case class Gear(adjacent1:Int, adjacent2:Int){
  val ratio:Int = adjacent1 * adjacent2
}

class EngineSchematic(val raw:List[List[Char]]){

  def getNumbers():List[NumberInEngine] = {
    raw.zipWithIndex.flatMap{
      case (line, rowIndex) =>
        val (list, remaining) = line.map(char => Try(char.toString.toInt).toOption).zipWithIndex.foldLeft((List.empty[NumberInEngine], List.empty[Int])){
          case ((numbersInEngine, digitsAcc), (Some(digit), _)) =>
            (numbersInEngine, digitsAcc :+ digit)
          case ((numbersInEngine, Nil), (None, _)) =>
            (numbersInEngine, List.empty)
          case ((numbersInEngine, headDigit :: tailDigit), (None, columnIndex)) =>
            val value = tailDigit.foldLeft(headDigit){
              case (acc, newDigit) => 10 * acc + newDigit
            }
            val numberInEngine = NumberInEngine(value, rowIndex, columnIndex - tailDigit.length - 1, columnIndex -1)
            (numberInEngine :: numbersInEngine, List.empty)
        }
        remaining match {
          case headDigit :: tailDigit =>
            val value = tailDigit.foldLeft(headDigit) {
              case (acc, newDigit) => 10 * acc + newDigit
            }
            val numberInEngine = NumberInEngine(value, rowIndex, raw.head.length - tailDigit.length - 1, raw.head.length -1)
            numberInEngine :: list
          case _ => list
        }

    }
  }

  def getGears():List[Gear] = {
    val gearCandidates:List[(Int, Int)] = raw.zipWithIndex.flatMap{
      case (row, rowIndex) =>
        row.zipWithIndex.flatMap{
          case (char, columnIndex) =>
            if(char == '*') Some((rowIndex, columnIndex))
            else None
        }
    }

    gearCandidates.flatMap {
      case (rowCand, colCand) =>
        val adjacentNumbers =
          getNumbers()
            .filter(numInEngine => Math.abs(numInEngine.rowIndex - rowCand) <= 1 )
            .filter(numInEngine => {
              numInEngine.adjacents().contains((rowCand, colCand))
            })
        adjacentNumbers match {
          case List(adjacent1, adjacent2) => Some(Gear(adjacent1.value, adjacent2.value))
          case _ => None
        }
    }


  }

  def isAdjacentToSymbol(engineInNumber: NumberInEngine):Boolean = {
    engineInNumber.adjacents().exists(isSymbol)
  }

  def isSymbol(coordinate:(Int, Int)):Boolean = {
    Try(raw(coordinate._1)(coordinate._2)).map{
      char =>
        char != '.' && Try(char.toString.toInt).isFailure
    }.getOrElse(false)
  }
}

