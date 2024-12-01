package year_2023.day_022

object Main_2 extends App {
  val bricksCascade = Parser.readInput(isSample = false)


  val (bricksFallenCascade, _) = bricksCascade.letBricksFall()
  println(bricksFallenCascade.bricks.map{
    brick =>
      val upperBlocks = brick.blocks.map(b => b.copy(z = b.z + 1))
      if(upperBlocks.exists(b => !brick.blocks.contains(b) && b.z < bricksFallenCascade.zScanner.size && bricksFallenCascade.schema(b.x)(b.y)(b.z))) {
        val (_, fallingCount) = BricksCascade(bricksFallenCascade.bricks.filter(_ != brick)).letBricksFall()
        fallingCount
      }
      else 0
  }.sum)

}


//1058 too low