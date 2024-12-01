package year_2023.day_024

import scala.collection.mutable


/**
 * Components for the cartesian equation of a line as in
 * a*x + b*y = c
 * */
case class CartesianEquation2D(a:Double, b:Double, c:Double)
object AlgebraService {

  def getSolutions(h1: Hailstone, h2: Hailstone):Option[(Double, Double)] = {
    val cart1 = toCartesianEquation2D(h1)
    val cart2 = toCartesianEquation2D(h2)

    gaussianElimination(cart1, cart2)
      .filter{
        case (x,_) =>
          val lambda1 = (x - h1.initial.x)/h1.speed.x
          val lambda2 = (x - h2.initial.x)/h2.speed.x

          lambda1 > 0 && lambda2 > 0
      }
  }

  def gaussianElimination(cart1:CartesianEquation2D, cart2:CartesianEquation2D):Option[(Double, Double)] = {
    val extendedMatrix = mutable.ListBuffer(
      mutable.ListBuffer(cart1.a, cart1.b, cart1.c),
      mutable.ListBuffer(cart2.a, cart2.b, cart2.c)
    )

    (0 to 2).foreach{
      i =>
        extendedMatrix(1)(i) +=  extendedMatrix.head(i) * cart2.a / (- cart1.a)
    }


    if(extendedMatrix(1)(1) == 0) None
    else {
      val y = extendedMatrix(1)(2) / extendedMatrix(1)(1)
      val n0 = extendedMatrix.head(1)
      val p = extendedMatrix(1)(1)
      (1 to 2).foreach {
        i =>
          extendedMatrix.head(i) += extendedMatrix(1)(i) * n0 / (-p)
      }
      val x = extendedMatrix.head(2) / extendedMatrix.head.head
      Some((x,y))
    }
  }

  def toCartesianEquation2D(hailstone: Hailstone):CartesianEquation2D = {
    /**
     * Generative equations
     * x = x0 + lvx
     * y = y0 + lvy
     *
     * Solve into
     * x*(-vy) + y*vx = x0*(-vy) + y0*(vx)
     * */
    CartesianEquation2D(
      a = -hailstone.speed.y,
      b = hailstone.speed.x,
      c = -(hailstone.initial.x * hailstone.speed.y) + (hailstone.initial.y * hailstone.speed.x))
  }

  def discreteLineIntersection(l1:DiscreteLine, l2:DiscreteLine):Option[(BigInt, BigInt)] = {
    val extendedMatrix = mutable.ListBuffer(
      mutable.ListBuffer(-l1.v.x, l2.v.x, l1.p.x - l2.p.x),
      mutable.ListBuffer(-l1.v.y, l2.v.y, l1.p.y - l2.p.y),
      mutable.ListBuffer(-l1.v.z, l2.v.z, l1.p.z - l2.p.z)
    )


    var gcdValue: BigInt = 0L
    var firstRowMult: BigInt = 0L
    var secRowMult: BigInt = 0L
    var lambda1: Option[BigInt] = None
    var lambda2: Option[BigInt] = None

    //3rd row operations
    List(l1.v.x, l1.v.y, l1.v.z).zipWithIndex.find{
      case (valInMatrix, _) => valInMatrix != 0
    } match {
      case Some((_, targetRowIndex)) =>
        val firstRow = extendedMatrix.head
        val targetRow = extendedMatrix(targetRowIndex)
        extendedMatrix(0) = targetRow
        extendedMatrix(targetRowIndex) = firstRow
      case None =>
        return None
    }

    if(extendedMatrix(2)(0) != 0){
      gcdValue = gcd(extendedMatrix(0)(0), extendedMatrix(2)(0))
      firstRowMult = extendedMatrix(2)(0) / gcdValue
      secRowMult = extendedMatrix(0)(0) / gcdValue
      (0 until 3).foreach {
        i =>
          extendedMatrix(2)(i) = firstRowMult * extendedMatrix(0)(i) - secRowMult * extendedMatrix(2)(i)
      }
    }
    // a * lambda2 = b
    (extendedMatrix(2)(1), extendedMatrix(2)(2)) match {
      case (zero1, zero2) if zero1.toInt == 0 && zero2.toInt == 0 => // lambda keeps the value
      case (zero, b) if zero == 0 && b != 0 => return None
      case (a, b) if b % a == 0 => lambda2 = Some(b / a)
      case (a, b) if b % a != 0 => lambda2 = None
    }

    //2nd row operations
    List(l2.v.x, l2.v.y).zipWithIndex.find {
      case (valInMatrix, _) => valInMatrix != 0
    } match {
      case Some((_, targetRowIndex)) =>
        val firstRow = extendedMatrix.head
        val targetRow = extendedMatrix(targetRowIndex)
        extendedMatrix(0) = targetRow
        extendedMatrix(targetRowIndex) = firstRow
      case None =>
        return None
    }
    if(extendedMatrix(1)(0) != 0) {
      gcdValue = gcd(extendedMatrix(0)(0), extendedMatrix(1)(0))
      firstRowMult = extendedMatrix(1)(0) / gcdValue
      secRowMult = extendedMatrix(0)(0) / gcdValue
      (0 until 3).foreach { i =>
        extendedMatrix(1)(i) = firstRowMult * extendedMatrix(0)(i) - secRowMult * extendedMatrix(1)(i)
      }
    }
    // a * lambda2 = b
    (extendedMatrix(1)(1), extendedMatrix(1)(2), lambda2) match {
      case (zero1, zero2, _) if zero1.toInt == 0 && zero2.toInt == 0 => //lambda keeps the value
      case (zero, b, _) if zero.toInt == 0 && b != 0 => return None
      case (a, b, Some(lambdaValue)) if lambdaValue == b / a => // lambda keeps the value
      case (a, b, Some(lambdaValue)) if lambdaValue != b / a => return None
      case (a, b, None) if b % a == 0 => lambda2 = Some(b / a)
      case (a, b, None) if b % a != 0 => return None
    }

    //1rst row operations
    // a * lambda1 = b
    (extendedMatrix(1)(1), extendedMatrix(1)(2)) match {
      case (zero1, zero2) if zero1.toInt == 0 && zero2.toInt == 0 => //lambda keeps the value
      case (zero, b) if zero == 0 && b != 0 => return None
      case (a, b) if b % a == 0 => lambda1 = Some(b / a)
      case (a, b) if b % a != 0 => return None
    }

    Some(lambda1.getOrElse(0), lambda2.getOrElse(0))

  }

  /**
   * We have an origin point p and want to find a candidate line that:
   * - Has p as origin
   * - Intersects the line l2 = p2 + lambda2 * v2
   * - Intersects the line l3 = p3 + lambda3 * v3
   *
   * We consider a line l1 for each value of lambda2
   * l1(lambda2) = p + lambda1 * (p2 + lambda2 * v2 - p)
   *
   * We consider the system l1(lambda2) = l3 and we get a system with:
   * A * lambda1 + B * lambda2 * lambda1 + C * lambda3 = D
   * With:
   * A = p2 - p
   * B = v2
   * C = -v3
   * D = p3 - p
   *
   * We consider the variable change lambda4 =  lambda2 * lambda1
   * We find solutions to the system and if there is one and we can divide lambda4 by lambda1 we have a solution,
   * otherwise there is not
   *
   * This should speed it up!
   * */
  def findLineCandidate(p:Point, l2: DiscreteLine, discreteLines: List[DiscreteLine]): Option[DiscreteLine] = {
    def buildExtendedMatrix(l3: DiscreteLine): mutable.ListBuffer[mutable.ListBuffer[BigInt]] =
      mutable.ListBuffer(
        mutable.ListBuffer(l2.p.x - p.x, l2.v.x, -l3.v.x, l3.p.x - p.x),
        mutable.ListBuffer(l2.p.y - p.y, l2.v.y, -l3.v.y, l3.p.y - p.y),
        mutable.ListBuffer(l2.p.z - p.z, l2.v.z, -l3.v.z, l3.p.z - p.z)
      )

    val extendedMatrix =
      discreteLines.collectFirst{
        case l3 if determinant(buildExtendedMatrix(l3)) != 0 =>
          buildExtendedMatrix(l3)
      }.getOrElse(return None)

    val permutation = mutable.ListBuffer(1 -> Option.empty[Rational], 4 -> Option.empty[Rational], 3 -> Option.empty[Rational])

    def getLambda(lambdaN: Int): Option[Rational] = {
      val inPermutation = permutation.find(_._1 == lambdaN).getOrElse(throw new Exception(s"Don't have lambda '$lambdaN' in the system"))
      inPermutation._2
    }

    def getPerm(rowN: Int): Option[Rational] = {
      permutation(rowN)._2
    }

    def setPerm(rowN:Int, value:Option[Rational]):Unit = {
      permutation(rowN) = permutation(rowN)._1 -> value
    }


    var gcdValue: BigInt = 0L
    var firstRowMult: BigInt = 0L
    var secRowMult: BigInt = 0L

    List(extendedMatrix(0)(0), extendedMatrix(0)(1), extendedMatrix(0)(2)).zipWithIndex.find {
      case (valInMatrix, _) => valInMatrix != 0
    } match {
      case Some((_, targetColumnIndex)) =>
        val firstColumn = (0 until 3).map(i => extendedMatrix(0)(i))
        val targetColumn = (0 until 3).map(i => extendedMatrix(targetColumnIndex)(i))
        (0 until 3).foreach(i => extendedMatrix(0)(i) = targetColumn(i))
        (0 until 3).foreach(i => extendedMatrix(targetColumnIndex)(i) = firstColumn(i))

        val firstPermutation = permutation.head
        val targetPermutation = permutation(targetColumnIndex)
        permutation(0) = targetPermutation
        permutation(targetColumnIndex) = firstPermutation
      case None =>
        return None
    }

    if (extendedMatrix(2)(0) != 0) {
      gcdValue = gcd(extendedMatrix(0)(0), extendedMatrix(2)(0))
      firstRowMult = extendedMatrix(2)(0) / gcdValue
      secRowMult = extendedMatrix(0)(0) / gcdValue
      (0 until 4).foreach {
        i =>
          extendedMatrix(2)(i) = firstRowMult * extendedMatrix(0)(i) - secRowMult * extendedMatrix(2)(i)
      }
    }

    if (extendedMatrix(1)(0) != 0) {
      gcdValue = gcd(extendedMatrix(0)(0), extendedMatrix(1)(0))
      firstRowMult = extendedMatrix(1)(0) / gcdValue
      secRowMult = extendedMatrix(0)(0) / gcdValue
      (0 until 4).foreach {
        i =>
          extendedMatrix(1)(i) = firstRowMult * extendedMatrix(0)(i) - secRowMult * extendedMatrix(1)(i)
      }
    }

    //2nd row operations
    List(extendedMatrix(1)(1), extendedMatrix(1)(2)).zipWithIndex.find {
      case (valInMatrix, _) => valInMatrix != 0
    } match {
      case Some((_, targetColumnIndex)) =>
        val firstRow = (0 until 3).map(i => extendedMatrix(1)(i))
        val targetColumn = (0 until 3).map(i => extendedMatrix(targetColumnIndex + 1)(i))
        (0 until 3).foreach(i => extendedMatrix(1)(i) = targetColumn(i))
        (0 until 3).foreach(i => extendedMatrix(targetColumnIndex + 1)(i) = firstRow(i))

        val firstPermutation = permutation(1)
        val targetPermutation = permutation(targetColumnIndex + 1)
        permutation(1) = targetPermutation
        permutation(targetColumnIndex + 1) = firstPermutation
      case None =>
        return None
    }
    if (extendedMatrix(2)(1) != 0) {
      gcdValue = gcd(extendedMatrix(2)(1), extendedMatrix(1)(1))
      firstRowMult = extendedMatrix(2)(1) / gcdValue
      secRowMult = extendedMatrix(1)(1) / gcdValue
      (0 until 4).foreach { i =>
        extendedMatrix(2)(i) = firstRowMult * extendedMatrix(1)(i) - secRowMult * extendedMatrix(2)(i)
      }
    }

    // a * lambda3 = b
    (extendedMatrix(2)(2), extendedMatrix(2)(3)) match {
      case (zero, _) if zero.toInt == 0 =>
        return None
      case (a, b) if b % a == 0 =>
        setPerm(2, Some(Rational(b,a)))
      case (a, b) if b % a != 0 =>
        return None
    }


    // a * lambda2 = b
    (extendedMatrix(1)(1), getPerm(2).get.product(-extendedMatrix(1)(2)).plus(extendedMatrix(1)(3))) match {
      case (zero, _) if zero.toInt == 0 =>
        return None
      case (a, b) =>
        setPerm(1, Some(b.divide(a)))
    }

    //1rst row operations
    // a * lambda1 = b
    (extendedMatrix(0)(0),
      getPerm(1).get.product(-extendedMatrix(0)(1)).plus(getPerm(2).get.product(-extendedMatrix(0)(2)).plus(extendedMatrix(0)(3)))) match {
      case (zero, _) if zero.toInt == 0 =>
        return None
      case (a, b) =>
        setPerm(0, Some(b.divide(a)))
    }

    val lambda4 = getLambda(4).get
    val lambda1 = getLambda(1).get

    val lambda2 = lambda4.divide(lambda1).normalize()

    val normLambda2 = lambda2.product(gcd(gcd(l2.v.x, l2.v.y), l2.v.z))
    val normL2 = DiscreteLine(l2.p, l2.v.normalize())

    Option.when(normLambda2.a != 0 && normLambda2.b.abs == 1)(
      DiscreteLine(
        p,
        (normL2.pointAt(normLambda2.a * normLambda2.b) + p.vectorFromOrigin.byScalar(-1)).vectorFromOrigin.normalize()
      )
    )

  }

  def findLineCandidateDecimal(p: Point, l2: DiscreteLine, discreteLines: List[DiscreteLine]): Option[DecimalLine] = {
    val pd = p.toDecimal
    val l2d = l2.toDecimal
    def buildExtendedMatrix(l3: DiscreteLine): mutable.ListBuffer[mutable.ListBuffer[BigInt]] =
      mutable.ListBuffer(
        mutable.ListBuffer(l2.p.x - p.x, l2.v.x, -l3.v.x, l3.p.x - p.x),
        mutable.ListBuffer(l2.p.y - p.y, l2.v.y, -l3.v.y, l3.p.y - p.y),
        mutable.ListBuffer(l2.p.z - p.z, l2.v.z, -l3.v.z, l3.p.z - p.z)
      )

    val extendedMatrix =
      discreteLines.collectFirst {
        case l3 if determinant(buildExtendedMatrix(l3)) != 0 =>
          buildExtendedMatrix(l3).map{
            _.map(d => BigDecimal(d))
          }
      }.getOrElse(return None)

    val permutation = mutable.ListBuffer(1 -> Option.empty[BigDecimal], 4 -> Option.empty[BigDecimal], 3 -> Option.empty[BigDecimal])

    def getLambda(lambdaN: Int): Option[BigDecimal] = {
      val inPermutation = permutation.find(_._1 == lambdaN).getOrElse(throw new Exception(s"Don't have lambda '$lambdaN' in the system"))
      inPermutation._2
    }

    def getPerm(rowN: Int): Option[BigDecimal] = {
      permutation(rowN)._2
    }

    def setPerm(rowN: Int, value: Option[BigDecimal]): Unit = {
      permutation(rowN) = permutation(rowN)._1 -> value
    }


    var firstRowMult: BigDecimal = 0L
    var secRowMult: BigDecimal = 0L

    List(extendedMatrix(0)(0), extendedMatrix(0)(1), extendedMatrix(0)(2)).zipWithIndex.find {
      case (valInMatrix, _) => valInMatrix != 0
    } match {
      case Some((_, targetColumnIndex)) =>
        val firstColumn = (0 until 3).map(i => extendedMatrix(0)(i))
        val targetColumn = (0 until 3).map(i => extendedMatrix(targetColumnIndex)(i))
        (0 until 3).foreach(i => extendedMatrix(0)(i) = targetColumn(i))
        (0 until 3).foreach(i => extendedMatrix(targetColumnIndex)(i) = firstColumn(i))

        val firstPermutation = permutation.head
        val targetPermutation = permutation(targetColumnIndex)
        permutation(0) = targetPermutation
        permutation(targetColumnIndex) = firstPermutation
      case None =>
        return None
    }

    if (extendedMatrix(2)(0) != 0) {
      firstRowMult = extendedMatrix(2)(0)
      secRowMult = extendedMatrix(0)(0)
      (0 until 4).foreach {
        i =>
          extendedMatrix(2)(i) = firstRowMult * extendedMatrix(0)(i) - secRowMult * extendedMatrix(2)(i)
      }
    }

    if (extendedMatrix(1)(0) != 0) {
      firstRowMult = extendedMatrix(1)(0)
      secRowMult = extendedMatrix(0)(0)
      (0 until 4).foreach {
        i =>
          extendedMatrix(1)(i) = firstRowMult * extendedMatrix(0)(i) - secRowMult * extendedMatrix(1)(i)
      }
    }

    //2nd row operations
    List(extendedMatrix(1)(1), extendedMatrix(1)(2)).zipWithIndex.find {
      case (valInMatrix, _) => valInMatrix != 0
    } match {
      case Some((_, targetColumnIndex)) =>
        val firstRow = (0 until 3).map(i => extendedMatrix(1)(i))
        val targetColumn = (0 until 3).map(i => extendedMatrix(targetColumnIndex + 1)(i))
        (0 until 3).foreach(i => extendedMatrix(1)(i) = targetColumn(i))
        (0 until 3).foreach(i => extendedMatrix(targetColumnIndex + 1)(i) = firstRow(i))

        val firstPermutation = permutation(1)
        val targetPermutation = permutation(targetColumnIndex + 1)
        permutation(1) = targetPermutation
        permutation(targetColumnIndex + 1) = firstPermutation
      case None =>
        return None
    }
    if (extendedMatrix(2)(1) != 0) {
      firstRowMult = extendedMatrix(2)(1)
      secRowMult = extendedMatrix(1)(1)
      (0 until 4).foreach { i =>
        extendedMatrix(2)(i) = firstRowMult * extendedMatrix(1)(i) - secRowMult * extendedMatrix(2)(i)
      }
    }

    // a * lambda3 = b
    setPerm(2, Some(extendedMatrix(2)(3)/extendedMatrix(2)(2)))
    setPerm(1, Some((getPerm(2).get * (-extendedMatrix(1)(2)) + extendedMatrix(1)(3))/extendedMatrix(1)(1)))
    setPerm(0, Some((getPerm(1).get * (-extendedMatrix(0)(1)) + getPerm(2).get * (-extendedMatrix(0)(2)) + extendedMatrix(0)(3))/extendedMatrix(0)(0)))

    // a * lambda2 =

    //1rst row operations
    // a * lambda1 = b


    val lambda4 = getLambda(4).get
    val lambda1 = getLambda(1).get
//    val lambda2 = lambda4 / lambda1

    Option.when(lambda1 != 0)(
      DecimalLine(
        pd,
        (l2d.pointAt(lambda4 / lambda1) + pd.vectorFromOrigin.byScalar(-1)).vectorFromOrigin
      )
    )

  }

  def determinant(m:mutable.ListBuffer[mutable.ListBuffer[BigInt]]):BigInt = {
    m(0)(0) * m(1)(1) * m(2)(2) +
      m(1)(0) * m(2)(1) * m(0)(2) +
      m(2)(0) * m(0)(1) *  m(1)(2) -
      (
        m(2)(0) * m(1)(1) * m(0)(2) +
          m(1)(0) * m(0)(1) * m(2)(2) +
          m(0)(0) * m(2)(1) * m(1)(2)
      )
  }

  def gcd(a:BigInt, b:BigInt):BigInt = {
    if(a.abs == 0) b.abs
    else gcd(b % a, a).abs
  }

  def minDistance(dist:BigInt => BigDecimal):(BigInt, BigDecimal) = {
    var i:BigInt = 0
    val (distancePlus, distanceZero) = (dist(10), dist(0))
    if(distancePlus < distanceZero) i = 10
    else i = -10

    var distI_1 = dist(i)
    i*=10
    var distI = dist(i)
    while(distI < distI_1) {
      distI_1 = distI
      i *= 10
      distI = dist(i)
    }
    var bigI = i
    var smallI = i / 10

    while((bigI - smallI).abs > 1){
      val cand1 = smallI
      val cand2 = (smallI + bigI) / 2

      if(dist(cand1) > dist(cand2)) {
        bigI = cand1
        smallI = cand2
      }
      else{
        bigI = cand2
        smallI = cand1
      }
    }

    (smallI, dist(smallI))
  }

}
