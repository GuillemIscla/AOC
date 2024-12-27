package year_2024.day_025

object Main1 extends App {

  val input = Parser.readInput(isSample = false)

  val (locks, keys) = input.foldLeft((List.empty[Schematic], List.empty[Schematic])){
    case ((locksAcc, keysAcc), schematic: Schematic) if schematic.isLock =>
      (locksAcc :+ schematic, keysAcc)
    case ((locksAcc, keysAcc), schematic: Schematic) =>
      (locksAcc, keysAcc :+ schematic)
  }

  val matches =
    for {
      lock <- locks
      key <- keys
    } yield isMatch(lock, key, lock.height)

  println(matches.count(thisMatch => thisMatch))


  def isMatch(key:Schematic, lock:Schematic, height:Int):Boolean = {
    key.heights.zip(lock.heights).forall{
      case (a, b) => a + b < height -1
    }
  }

}
