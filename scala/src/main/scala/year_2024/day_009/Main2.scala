package year_2024.day_009

object Main2 extends App {
  val input = Parser.readInput(isSample = false)

  val firstFileToMove = input.raw.flatMap{
    case File(id, _, _) => Some(id)
    case _ => None
  }.max

  var currentMap = input
  var fileToMove = firstFileToMove
  while(fileToMove >= 0){
    currentMap = currentMap.moveFile(fileToMove)
    fileToMove -= 1
  }

  println(currentMap.checksum)

}
