package year_2023.day_014

import scala.annotation.tailrec
import scala.collection.mutable

trait RockItem {
  def raw:Char
}

case object RoundedRock extends RockItem {
  override def raw: Char = 'O'
}

case object CubeRock extends RockItem {
  override def raw: Char = '#'
}


case object Empty extends RockItem {
  override def raw: Char = '.'
}

object RockItem {

  val all:List[RockItem] = List(RoundedRock, CubeRock, Empty)
  def fromRaw(raw:Char):RockItem = all.find(_.raw == raw).getOrElse(throw new Exception(s"Couldn't find what rock item is this: '$raw'"))
}

case class RockField(map:List[List[RockItem]]) {

  override def toString: String = map.map(_.map(_.raw).mkString).mkString("\n")

  def isEqual(other:RockField):Boolean = map.indices.forall {
    i => map.head.indices.forall {
      j => map(i)(j) == other.map(i)(j)
    }
  }
  def tiltNorth:RockField = {
    val mutableMap:mutable.ListBuffer[mutable.ListBuffer[RockItem]] = mutable.ListBuffer.from(map.map(mutable.ListBuffer.from))

    var j = 0
    while(j < mutableMap.head.size) {
      var i = 0
      var lastStopper = -1
      while (i < mutableMap.size) {
        if(mutableMap(i)(j) == CubeRock) {
          lastStopper = i
        }
        if(mutableMap(i)(j) == RoundedRock) {
          mutableMap(i)(j) = Empty
          mutableMap(lastStopper + 1)(j) = RoundedRock
          lastStopper += 1
        }
        i += 1
      }
      j += 1
    }
    RockField(List.from(mutableMap.map(List.from)))
  }

  def tiltEast:RockField = flipDiagonal.flipVertical.tiltNorth.flipVertical.flipDiagonal
  def tiltWest:RockField = flipDiagonal.tiltNorth.flipDiagonal
  def tiltSouth:RockField = flipVertical.tiltNorth.flipVertical

  def flipHorizontal:RockField = {
    val newMap:List[List[RockItem]] = map.indices.map{
      i =>
        map.head.indices.map{
          j => map(i)(map.head.size - j -1)
        }.toList
    }.toList

    RockField(newMap)
  }

  def flipVertical:RockField = {
    val newMap: List[List[RockItem]] = map.indices.map {
      i =>
        map.head.indices.map {
          j => map(map.size - i -1)(j)
        }.toList
    }.toList

    RockField(newMap)
  }

  /**
   * North -> West
   * West -> North
   * South -> East
   * East -> South
   * */
  def flipDiagonal:RockField = {
    val newMap: List[List[RockItem]] = map.head.indices.map {
      j =>
        map.indices.map {
          i => map(i)(j)
        }.toList
    }.toList

    RockField(newMap)
  }

  final def cicle(iterations:Int):RockField = {

    var currentField = this
    var iterationsRemaining = iterations
    var pastIterations:List[RockField] = List.empty
    var reduceUsed = false
    while(iterationsRemaining > 0){
      currentField = currentField.tiltNorth.tiltWest.tiltSouth.tiltEast
      iterationsRemaining -= 1
      if(!reduceUsed){
        pastIterations.zipWithIndex.find(_._1.isEqual(currentField)).map(_._2).foreach {
          cicleFound =>
            iterationsRemaining = iterationsRemaining % (cicleFound + 1)
            reduceUsed = true
        }
      }
      pastIterations = currentField.copy() :: pastIterations
    }
    currentField
  }


  def calculateLoad:Long = map.indices.map{
    i => map.head.indices.map{
      j =>
        map(i)(j) match {
          case RoundedRock => map.size - i
          case _ => 0
        }
    }.sum
  }.sum
}
