package year_2024.day_020

//42431493 too high
//42405039 too high
//42390758 too high
//23753211 not right
//23423427 not right
//24081140 not right

object Main2 extends App {

  val race = Parser.readInput(isSample = false)

  val indexedPath = race.path.zipWithIndex

  val maxCheatTime = 20
  val minCheatValue = 100


  println(indexedPath.map{
    case (startCheatPosition, startCheatValue) =>
      indexedPath.drop(startCheatValue + minCheatValue).count{
        case (endCheatPosition, endCheatValue) =>
          canCheatReachWithin(
            startCheatPosition,
            endCheatPosition,
            Math.min(
              maxCheatTime, // The cheat would be invalid if more
              endCheatValue - startCheatValue - minCheatValue // The cheat would not worth if more
            )
          )
      }
  }.sum)

  def canCheatReachWithin(startCheatPosition: Position, endCheatPosition: Position, cheatSize: Int): Boolean = {
    val distance = Math.abs(startCheatPosition.x - endCheatPosition.x) + Math.abs(startCheatPosition.y - endCheatPosition.y)
    distance <= cheatSize
  }

}
