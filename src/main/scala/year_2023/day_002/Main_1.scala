package year_2023.day_002

import scala.io.Source



case class Game(id:Int, red:Int, green:Int, blue:Int){
  def isPossible(target:Sample):Boolean =
    red <= target.red &&
      green <= target.green &&
      blue <= target.blue
  def power(): Int = red * green * blue
}

object Game {
  def apply(id:Int, samples:List[Sample]):Game = {
    Game(id = id, red = samples.map(_.red).max, green = samples.map(_.green).max, blue =samples.map(_.blue).max)
  }
}


case class Sample(red:Int, green:Int, blue:Int){
  import Math._
  def +(other:Sample):Sample = Sample(red = max(red, other.red), green = max(green, other.green), blue = max(blue, other.blue))
}

object Main_1 extends App {
  val target = Sample(red = 12, green = 13, blue = 14)

  println(readInput(isSample = false).filter(_.isPossible(target)).map(_.id).sum)

  def readInput(isSample:Boolean):List[Game] =
    if (isSample)
      """Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green \n
        |Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue \n
        |Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red \n
        |Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red \n
        |Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green""".stripMargin.split("\n").toList.map(parseInput)
    else
      Source.fromResource("year_2023/day_002_input.txt").getLines().toList.map(parseInput)

  def parseInput(line:String):Game = {
    val (gameId, rawSamples) = parseLineIntoIterator(line)
    val samples = rawSamples.map(parseSample)
    Game(gameId, samples)
  }
  def parseLineIntoIterator(line:String):(Int, List[String]) = {
    val regex1 = """Game ([0-9]+):(.*)""".r

    regex1.findFirstMatchIn(line).map(_.subgroups) match {
      case Some(List(gameId, list)) =>
        (gameId.toInt, list.split(";").toList)
      case other => throw new Exception(s"Could not parse: '$line'")
    }
  }
  def parseSample(sampleRaw:String):Sample = {
    val regex2 = " ([0-9]+) ([a-z]+)".r
    sampleRaw.split(",").flatMap(regex2.findFirstMatchIn).map(_.subgroups).foldLeft(Sample(0,0,0)){
      case (acc, List(number, "blue")) => acc.copy(blue = acc.blue + number.toInt)
      case (acc, List(number, "red")) => acc.copy(red = acc.red + number.toInt)
      case (acc, List(number, "green")) => acc.copy(green = acc.green + number.toInt)
      case _ => throw new Exception(s"Could not parse '$sampleRaw'")
    }
  }
}
