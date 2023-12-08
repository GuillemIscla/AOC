package year_2023.day_005



object Main_2 extends App {
  val (seedList, mapChain) = Parser.readInput(isSample = false, withRanges = true)

  def processRange(pair:(Long, Long)):Long = {
    var index = 0
    var min = mapChain.chainInput(pair._1)
    while (index < pair._2){
      index += 1
      min = Math.min(mapChain.chainInput(pair._1 + index), min)
    }
    println(s"Processed range starting with ${pair._1}, being $min")
    min
  }

  println(seedList.pairList.map(processRange).min)

//  println(1)
//
//  val total = seedList.pairList.headOption.toList.flatMap {
//    case (start, range) => List.range(start, start + range)
//  }.length
//  println(total)
//
//  var i = 0
//
//  println(seedList.pairList.headOption.toList.flatMap{
//    case (start, range) => List.range(start, start + range)
//  }.map{
//    s =>
//      mapChain.chainInput(s)
//      i += 1
//      if(i % 10000 == 0) println((i.toDouble / total.toDouble))
//  }.min)

}
