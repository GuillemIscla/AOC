package year_2024.day_012

// 814074 too low

object Main2 extends App {

  val input = Parser.readInput(isSample = None)

  val areas = input.getAllAreas

  val areasWithFences = areas.map {
    area =>
      (area, input.getAllFences(area).filter {
        case VerticalFence(vX, (vYLeft, vYRight), _) =>
          !(area.exists {
            case Position(pX, pY, _) =>
              vX == pX && vYLeft.contains(pY)
          } &&
            area.exists {
              case Position(pX, pY, _) =>
                vX == pX && vYRight.contains(pY)
            })
        case HorizontalFence((vXLeft, vXRight), vY, _) =>
          !(area.exists {
            case Position(pX, pY, _) =>
              vY == pY && vXLeft.contains(pX)
          } &&
            area.exists {
              case Position(pX, pY, _) =>
                vY == pY && vXRight.contains(pX)
            })
      })
  }
  
  val areasWithSides = areasWithFences.map{
    case (area, fences) => (area, input.fencesToSides(fences))
  }

  println(areasWithSides.map {
    case (area, sides) => area.size * sides.size
  }.sum)
}
