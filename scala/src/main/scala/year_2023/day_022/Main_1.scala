package year_2023.day_022

object Main_1 extends App {
  val bricksCascade = Parser.readInput(isSample = true)


  val (bricksFallenCascade, _) = bricksCascade.letBricksFall()
  val someBricksDisintegratedCascade = bricksFallenCascade.disintegrateBricks()

  println(bricksCascade.bricks.size - someBricksDisintegratedCascade.bricks.size)

}
