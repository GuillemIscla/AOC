package year_2023.day_004

import scala.io.Source
import scala.util.Try

object Parser {

  def readInput(isSample:Boolean): List[ScratchCard] = {
    if(isSample){
      """Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
        |Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
        |Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
        |Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
        |Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
        |Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11""".split("\n").toList.map(parseScratchCard)
    }
    else Source.fromResource("year_2023/day_004_input.txt").getLines().map(parseScratchCard).toList
  }

  def parseScratchCard(line:String):ScratchCard = {
    val regex = "Card\\s+([0-9]+):([0-9\\s]*)\\|(.*)".r
    regex.findFirstMatchIn(line) match {
      case Some(regexMatch) =>
        val id = Try(regexMatch.group(1).toInt).getOrElse(throw new Exception(s"Could not parse CardId '$line'"))
        val winningNumbers = Try(regexMatch.group(2).split(" ").flatMap(_.toIntOption).toList).getOrElse(throw new Exception(s"Could not parse winning numbers '$line'"))
        val actualNumbers = Try(regexMatch.group(3).split(" ").flatMap(_.toIntOption).toList).getOrElse(throw new Exception(s"Could not parse actual numbers '$line'"))
        ScratchCard(id, winningNumbers, actualNumbers)
      case None =>
        throw new Exception(s"Could not parse '$line'")
    }

  }


}
