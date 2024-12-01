package year_2023.day_022

import year_2023.helper.ListUtils.ListSyntax


case class Brick(blocks:List[Coordinate])

object Brick {
  def fromCoordinates(coord1:Coordinate, coord2:Coordinate):Brick = {
    val freeComponent = {
      if(coord1.x != coord2.x) 0
      else if(coord1.y != coord2.y) 1
      else 2
    }

    val free1 = coord1.refByComponent(freeComponent)
    val free2 = coord2.refByComponent(freeComponent)

    val range =
      if(free1 < free2) (free1 to free2) else (free2 to free1)

    Brick(range.map(
      i => coord1.copyByComponent(freeComponent, i)
    ).toList)
  }
}

object BricksCascade {
  def apply(bricks:List[Brick]):BricksCascade = {
    val blocksInCascade = bricks.flatMap(_.blocks).toSet
    if(blocksInCascade.isEmpty){
      println()
    }
    val xRange = blocksInCascade.map(_.x).min to blocksInCascade.map(_.x).max
    val yRange = blocksInCascade.map(_.y).min to blocksInCascade.map(_.y).max
    val zRange = 0 to blocksInCascade.map(_.z).max
    val schema: List[List[List[Boolean]]] = {
      xRange.map {
        x =>
          yRange.map {
            y =>
              zRange.map {
                z => blocksInCascade.contains(Coordinate(x, y, z))
              }.toList
          }.toList
      }.toList
    }

    BricksCascade(bricks, blocksInCascade, xRange, yRange, zRange, schema)
  }
}
case class BricksCascade(
                          bricks:List[Brick],
                          blocksInCascade:Set[Coordinate],
                          xRange: Range.Inclusive,
                          yRange: Range.Inclusive,
                          zRange: Range.Inclusive,
                          schema:List[List[List[Boolean]]]
                        ) {
  val zScanner: List[List[Brick]] = {
    zRange.map {
      z => bricks.filter {
        brick => brick.blocks.exists {
          block => block.z == z
        }
      }
    }.toList
  }
  def letBricksFall():(BricksCascade, Int) = {
    zRange.flatMap(zScanner).distinct.foldLeft((this, 0)) {
      case ((auxCascade, count), newBrick) =>
        val fall = auxCascade.letBrickFall(newBrick)
        if (fall > 0) {
          (BricksCascade(
            auxCascade.bricks
              .replaceWhere(_ == newBrick,
                newBrick.copy(
                  blocks =
                    newBrick
                      .blocks
                      .map(coord => coord.copy(z = coord.z - fall)))
              )
          ), count + 1)
        }
        else {
          (auxCascade, count)
        }
    }
  }
  def disintegrateBricks():BricksCascade = {
    BricksCascade(
      bricks.filterNot {
        brickMaybeToDisintegrate =>
          howManyBricksWouldFall(brickMaybeToDisintegrate) == 0
      }
    )
  }

  def howManyBricksWouldFall(brickMaybeToDisintegrate:Brick): Int = {
    val myFilter = bricks.filter(_ != brickMaybeToDisintegrate)
    val cascadeWithoutThis = BricksCascade(myFilter)
    val zLayerToCheck = brickMaybeToDisintegrate.blocks.map(_.z).max + 1
    val bricksToCheck = if (zLayerToCheck == zScanner.size) List.empty else cascadeWithoutThis.bricks.filter(zScanner(zLayerToCheck).contains)
    bricksToCheck.count {
      brickMaybeFalls => cascadeWithoutThis.letBrickFall(brickMaybeFalls) > 0
    }
  }

  def letBrickFall(brick: Brick):Int = {
    var i = 0
    var finished = false
    val blocksToConsider = blocksInCascade.filterNot(b => brick.blocks.contains(b))
    while(brick.blocks.forall(_.z - i > 0) && !finished){
      i += 1
      finished = {
        brick.blocks.map(c => c.copy(z = c.z - i)).exists {
          blockInBrick => blocksToConsider.contains(blockInBrick)
        }
      }
    }
    Math.max(0, i - 1)
  }

}
