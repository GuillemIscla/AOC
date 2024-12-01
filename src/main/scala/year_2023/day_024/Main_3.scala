package year_2023.day_024

object Main_3 extends App {


  val discreteLines = Parser.readInputDiscrete(isSample = true)



//
//  val (dist, p1, p2) = getDistance(
//    DiscreteLine(Point(1, 2, 3), Vector(2, -1, 1)),
//    DiscreteLine(Point(4, -1, 5), Vector(1, 2, -3))
//  ).get
//
//  println(dist,p1,p2)
//  println(DecimalVector.from(p1, p2).norm)

//  discreteLines.indices.foreach {
//    i => println(s"${discreteLines(i)}, candidatePoint=${getCandidatePoint(i).toDiscrete}, lambda=${getCandidateLambda(i)}")
//  }

  def getCandidatePoint(lineIndex:Int):DecimalPoint = {
    var acc = DecimalVector(0, 0, 0)
    var count = 0
    val firstLine = discreteLines(lineIndex)
    discreteLines.indices.foreach {
      i =>
        val secondLine = discreteLines(i)

        val vectorialProduct = firstLine.v.vectorialProduct(secondLine.v)
        if (vectorialProduct.squareNorm > 0) {
          val (_, p1, _) = getDistance(firstLine, secondLine).get
          acc = acc + DecimalVector.from(p1)
          count += 1
        }
    }
    DecimalPoint(0,0,0) + acc.byScalar(BigDecimal(1) / count)
  }

  def getCandidateLambda(lineIndex:Int):BigInt = {

    val candidatePoint = getCandidatePoint(lineIndex)
    val firstLine = discreteLines(lineIndex)
    val coordPairs = List(
      (firstLine.pointAt(0).x, candidatePoint.x, firstLine.v.x),
      (firstLine.pointAt(0).y, candidatePoint.y, firstLine.v.y),
      (firstLine.pointAt(0).z, candidatePoint.z, firstLine.v.z),
    )

    val (lineCoord, candidCoord, vectorCoord) = coordPairs.find(_._3.abs > 0).get

    (candidCoord.toBigInt - lineCoord) / vectorCoord

  }


  def getDistance(l1: DiscreteLine, l2: DiscreteLine): Option[(BigDecimal, DecimalPoint, DecimalPoint)] = {
    val l1d = l1.toDecimal
    val l2d = l2.toDecimal
    val aToB = DecimalVector.from(l1d.p) + DecimalVector.from(l2d.p).byScalar(-1)
    val vectProduct = l1d.v.vectorialProduct(l2d.v)

    val distance = Option.when(vectProduct.norm != 0)((vectProduct.scalarProduct(aToB) / vectProduct.norm).abs)
    val lambda1 = l2d.v.vectorialProduct(vectProduct).scalarProduct(DecimalVector.from(l1d.p, l2d.p)) / (vectProduct.scalarProduct(vectProduct))
    val lambda2 = l1d.v.vectorialProduct(vectProduct).scalarProduct(DecimalVector.from(l1d.p, l2d.p)) / (vectProduct.scalarProduct(vectProduct))

    distance.map(d => (d, l1d.pointAt(lambda1), l2d.pointAt(lambda2)))
  }

}
