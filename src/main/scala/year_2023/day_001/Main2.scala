package year_2023.day_001

import scala.io.Source
import scala.util.Try


/** *
 * --- Day 1: Trebuchet?! ---
 * --- Part Two ---
Your calculation isn't quite right. It looks like some of the digits are actually spelled out with letters: one, two, three, four, five, six, seven, eight, and nine also count as valid "digits".

Equipped with this new information, you now need to find the real first and last digit on each line. For example:

two1nine
eightwothree
abcone2threexyz
xtwone3four
4nineeightseven2
zoneight234
7pqrstsixteen
In this example, the calibration values are 29, 83, 13, 24, 42, 14, and 76. Adding these together produces 281.

What is the sum of all of the calibration values?
 * */
object Main2 extends App {
  val stringToNumber = List(
    "nineight" -> "98",
    "eightwo" -> "82",
    "eighthree" -> "83",
    "twone" -> "21",
    "oneight" -> "18",
    "one" -> "1",
    "two" -> "2",
    "three" -> "3",
    "four" -> "4",
    "five" -> "5",
    "six" -> "6",
    "seven" -> "7",
    "eight" -> "8",
    "nine" -> "9"
  )

  println(readInput(isSample = false).map(parseWord).sum)



  def readInput(isSample:Boolean):List[String] =
    if(isSample)
      List("wo1nine",
        "eightwothree",
        "abcone2threexyz",
        "xtwone3four",
        "4nineeightseven2",
        "zoneight234",
        "7pqrstsixteen")
    else Source.fromResource("day1_input.txt").getLines().toList


//54498 too high
  //54489 not right
  //54424 not right
  //54473

  def parseWord(line:String):Int = {
    val line_1 = stringToNumber.foldLeft(line){
      case (aux, (string, number)) =>
        aux.replace(string, number)
    }
    val digitLine = line_1.toArray.flatMap(c => Try(c.toString.toInt).toOption).toList
    digitLine.head * 10 + digitLine.last
  }
}
