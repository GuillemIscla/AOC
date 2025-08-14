package year_2024.day_019

import scala.collection.mutable

object Main2 extends App {

  val (initialTowels, designs) = Parser.readInput(isSample = false)

  val memory = mutable.HashMap("" -> 1L)

  println(designs.map(arrangements).sum)

  def arrangements(design: Design):Long = {
    val designRaw = design.stripes.mkString
    memory.get(designRaw) match {
      case Some(value) => value
      case None =>
        val result = initialTowels.flatMap {
          towel =>
            val towelRaw = towel.stripes.mkString
            if(designRaw.startsWith(towelRaw)) Some(Design(designRaw.drop(towelRaw.length).toList))
            else None
        }.map(arrangements).sum
        memory.addOne(designRaw -> result)
        result
    }
  }
}
