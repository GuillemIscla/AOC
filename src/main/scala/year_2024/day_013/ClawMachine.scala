package year_2024.day_013

case class Button(offsetX:BigInt, offsetY:BigInt, token:BigInt)

case class Prize(x:BigInt, y:BigInt)

case class ClawMachine(buttonA:Button, buttonB:Button, prize:Prize){
  val diophanticX: DiophanticGeneric = DiophanticGeneric(buttonA.offsetX, buttonB.offsetX, prize.x)
  val diophanticY: DiophanticGeneric = DiophanticGeneric(buttonA.offsetY, buttonB.offsetY, prize.y)

  def strategies(maxTries:BigInt):List[(BigInt, BigInt, BigInt)] = {
    (for {
      lineX <- diophanticX.solutionLine
      lineY <- diophanticY.solutionLine
    } yield {
      lineX.pointsBetween(0, maxTries).intersect(lineY.pointsBetween(0, maxTries))
    }).getOrElse(Nil).map{
      case (a, b) => (a, b, a * buttonA.token + b * buttonB.token)
    }
  }

  def strategiesWithIntersecting: List[(BigInt, BigInt, BigInt)] = {
    (for {
      lineX <- diophanticX.solutionLine.toList
      lineY <- diophanticY.solutionLine.toList
      k_x <- lineX.kForIntersection(lineY).toList
      pointsAroundX <- lineX.pointsAround(k_x, 20)
      k_y <- lineY.kForIntersection(lineX).toList
      pointsAroundY <- lineY.pointsAround(k_y, 20)
      if(pointsAroundX == pointsAroundY)
    } yield pointsAroundX).map {
      case (a, b) => (a, b, a * buttonA.token + b * buttonB.token)
    }
  }

}

case class Line(p_x:BigInt, p_y:BigInt, v_x:BigInt, v_y:BigInt){
  def generatePoint(k:BigInt):(BigInt, BigInt) = (p_x + k * v_x, p_y + k * v_y)
  def pointsBetween(min:BigInt, max:BigInt):List[(BigInt, BigInt)] = {
    val lowerK = (min - p_x) / v_x
    val distance = (max - min) / v_x
    (lowerK - distance - 1 to lowerK + distance + 1).map(generatePoint).filter{
      case (x, y) =>
        x >= min && x <= max &&
        y >= min
    }.toList
  }

  def pointsAround(k: BigInt, margin: BigInt): List[(BigInt, BigInt)] =
    (k - margin to k + margin).map(generatePoint).toList

  def kForIntersection(other:Line):Option[BigInt] = {
    val determinant = v_x * other.v_y - v_y * other.v_x
    Option.when(determinant.toInt != 0) {
      ((other.p_x - p_x) * other.v_y - (other.p_y - p_y) * other.v_x) / determinant
    }
  }
}


//Equation of type a*x + b*y = c
case class DiophanticGeneric(a:BigInt, b:BigInt, c:BigInt){


  private val (bez_x, bez_y, gcd): (BigInt, BigInt, BigInt) = {
    var r_i = a
    var r_i_1 = b
    var s_i = BigInt(1)
    var s_i_1 = BigInt(0)
    var t_i = BigInt(0)
    var t_i_1 = BigInt(1)

    while (r_i_1 != 0) {
      val quotient = r_i / r_i_1
      val r_temp = r_i
      r_i = r_i_1
      r_i_1 = r_temp - quotient * r_i_1

      val s_temp = s_i
      s_i = s_i_1
      s_i_1 = s_temp - quotient * s_i_1

      val t_temp = t_i
      t_i = t_i_1
      t_i_1 = t_temp - quotient * t_i_1
    }

    (s_i, t_i, r_i)
  }

  private val maybeFirstSolution:Option[(BigInt, BigInt)] = {
    if(c % gcd == 0) {
      Some((bez_x * c / gcd, bez_y * c / gcd))
    }
    else None
  }

  val solutionLine:Option[Line] = maybeFirstSolution.map{
    case (p_x, p_y) => Line(p_x, p_y, b / gcd, -a / gcd)
  }
}
