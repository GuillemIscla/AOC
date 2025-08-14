package year_2023.day_005

import scala.io.Source
import scala.util.Try

object Parser {

  def readInput(isSample:Boolean, withRanges:Boolean = false): (Seeds, MapChain) = {
    if(isSample){
      parseInput("""seeds: 79 14 55 13

        seed-to-soil map:
        50 98 2
        52 50 48

        soil-to-fertilizer map:
        0 15 37
        37 52 2
        39 0 15

        fertilizer-to-water map:
        49 53 8
        0 11 42
        42 0 7
        57 7 4

        water-to-light map:
        88 18 7
        18 25 70

        light-to-temperature map:
        45 77 23
        81 45 19
        68 64 13

        temperature-to-humidity map:
        0 69 1
        1 0 69

        humidity-to-location map:
        60 56 37
        56 93 4""".split("\n").toList, withRanges)
    }
    else parseInput(Source.fromResource("year_2023/day_005_input.txt").getLines().toList, withRanges)
  }

  def parseInput(input:List[String], withRanges:Boolean):(Seeds, MapChain) = {
    val seeds = parseSeeds(input.head, withRanges)

    val rawGardenMaps:List[List[String]] = input.tail.foldLeft(List.empty[List[String]]){
      case (Nil, "") => Nil
      case (Nil, newString) => List(List(newString))
      case (gardenMapsRaw, "") => gardenMapsRaw :+ Nil
      case (init :+ last, newString) => init :+ (last :+ newString)
    }

    val gardenMaps = rawGardenMaps.map(parseGardenMap)

    (seeds, MapChain.fromUnsorted(gardenMaps))
  }

  def parseSeeds(seedLine:String, withRanges:Boolean):Seeds = {
    if(withRanges){
      val listMixed =
        Try {
          val rawInts = seedLine.split(":")(1)
          rawInts.split(" ").flatMap(_.toLongOption).toList
        }.getOrElse(throw new Exception(s"Could not parse the seeds with this: '$seedLine'"))

      val pairList = listMixed.foldLeft((List.empty[(Long, Long)], Option.empty[Long])){
        case ((accList, Some(remainder)), newInt) => (accList :+ (remainder, newInt), None)
        case ((accList, None), newInt) => (accList, Some(newInt))
      }._1
      RangeSeeds(pairList)
    }
    else {
      ConsecutiveSeeds(Try {
        val rawInts = seedLine.split(":")(1)
        rawInts.split(" ").flatMap(_.toLongOption).toList
      }.getOrElse(throw new Exception(s"Could not parse the seeds with this: '$seedLine'")))
    }
  }

  def parseGardenMap(rawGardenMap:List[String]):GardenMap = {
    val mapNameRegex = "([a-z]+)\\-to\\-([a-z]+)".r
    val (from, to) = Try{
      val regexResult = mapNameRegex.findFirstMatchIn(rawGardenMap.head).get
      (regexResult.group(1), regexResult.group(2))
    }.getOrElse(throw new Exception(s"Could not parse the name of the GardenMap with this: '${rawGardenMap.head}'"))
    val mapSegments = rawGardenMap.tail.map{
      rawInts => Try{
        val List(destinationRangeStart, sourceRangeStart, rangeLength) = rawInts.split(" ").flatMap(_.toLongOption).toList
        MapSegment(destinationRangeStart, sourceRangeStart, rangeLength)
      }.getOrElse(throw new Exception(s"Could not parse a map segment with this line: '$rawInts' on map $from-to-$to"))
    }

    GardenMap(from, to, mapSegments)
  }


}
