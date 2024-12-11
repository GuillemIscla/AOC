package year_2024.day_009

object Main1 extends App {

  val input = Parser.readInput(isSample = false)

//  1957298123 too low
//  6341329027019 too low
//  16341329027019 too high
//  6339773965610 not right

  var currentMap = input

  while(currentMap.freeSpaceCount > 1 ){

    if(currentMap.freeSpaceCount < 10){
//      println(currentMap.raw.take(10))
      println(currentMap.raw.takeRight(10))
    }
    currentMap = currentMap.moveBlock
//    println(currentMap)
  }
//  println(currentMap.raw.take(10))
  println(currentMap.raw.takeRight(10))
  println(currentMap.checksum)

}
