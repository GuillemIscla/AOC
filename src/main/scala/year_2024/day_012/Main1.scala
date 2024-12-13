package year_2024.day_012

// 1302280 too low


object Main1 extends App {

  val input = Parser.readInput(isSample = Some(0))

  val areas = input.getAllAreas

  val areasWithFences = areas.map{
    area =>
      (area, input.getAllFences(area).filter{
        case VerticalFence(vX, (vYLeft, vYRight), _) =>
          ! (area.exists{
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

  println(areasWithFences.map{
    case (area, fences) => area.size * fences.size
  }.sum)

}
