package year_2023.day_024

import year_2023.day_024.AlgebraService._

import scala.util.Try

object Main_4 extends App {
  val discreteLines = Parser.readInputDiscrete(isSample = false)

  val firstLine = discreteLines(2)
  val secondLine = discreteLines(3)
  val otherLines = discreteLines.take(2) ++ discreteLines.drop(4)

  val from = -444385611362L

  discreteLines.indices.foreach{
    i =>
      (for {
        line <- findLineCandidateDecimal(discreteLines(i).pointAt(444385611362L), discreteLines((i + 1) % discreteLines.size), discreteLines)
        distance <- getDistance(DiscreteLine(line.p.toDiscrete, line.v.toDiscrete), otherLines.head)
      } yield distance._1.toDouble  ).foreach{
        d => println(s"i=$i, d=$d")
      }
  }

//  println(minDistance({i => {
//    for {
//      line <- findLineCandidateDecimal(firstLine.pointAt(i), secondLine, otherLines)
//      distance <- getDistance(DiscreteLine(line.p.toDiscrete, line.v.toDiscrete), otherLines.head)
//    } yield distance._1
//  }.get}))

//  usingCandidateLines2()

//  discreteLines.indices.foreach{
//    i =>
//      println(getCandidatePoint(i))
//  }


//  val resultLine = usingCandidateLines2()
//
//  resultLine match {
//    case Some(line) =>
//      println(line)
//      println(line.p.x + line.p.y + line.p.z)
//    case None =>
//      println("Could not find a line!")
//  }

  def usingCandidateLines():Option[DiscreteLine] = {

    var i = 0
    var resultLine: Option[DiscreteLine] = None

    while (i < discreteLines.size && resultLine.isEmpty) {
      val firstLine = discreteLines(i)
      val restLines1 = discreteLines.take(i) ++ discreteLines.drop(i + 1)
      var j = 0
      while (j < discreteLines.size && resultLine.isEmpty){
        val secondLine = discreteLines(j)
        val restLines2 = restLines1.take(j) ++ restLines1.drop(j + 1)
        findLineCandidate(firstLine.pointAt(1), secondLine, restLines2).foreach {
          resultCandidate =>
            getLambdas(resultCandidate, discreteLines).foreach {
              lambdaList =>
                resultLine = Some(resultCandidate.moveOrigin(lambdaList.min - 1))
            }
        }
        j += 1
      }
      i += 1
    }

    resultLine

  }

  def usingCandidateLines2(): Option[DiscreteLine] = {


    val toTry:Long = 1000L
    var i = 0
    var resultLine: Option[DiscreteLine] = None

    val firstLine = discreteLines.head
    val secondLine = discreteLines.tail.head
    val restLines = discreteLines.tail.tail
    val (lambdaCandidate, _) = (444385611362L, 2)

    while (i < toTry && resultLine.isEmpty) {
      val index = lambdaCandidate + i
      if (i % (toTry / 100) == 0) println(i, index)
      findLineCandidate(firstLine.pointAt(index), secondLine, restLines).foreach {
        resultCandidate =>
          getLambdas(resultCandidate, discreteLines).foreach {
            lambdaList =>
              resultLine = Some(resultCandidate.moveOrigin(lambdaList.min - 1))
          }
      }
      i += 1
    }
    resultLine
  }

  def bruteForce():Option[DiscreteLine] = {
    val firstLine = discreteLines.head
    val secondLine = discreteLines(1)

    val toTry = 50000

    var i = -toTry
    var j = 0L
    var resultLine: Option[DiscreteLine] = None

    while (i < toTry && resultLine.isEmpty) {
      j = -toTry
      if (i % 100 == 0) println(i)
      while (j < toTry && resultLine.isEmpty) {
        val resultCandidate = DiscreteLine.from(firstLine.pointAt(i), secondLine.pointAt(j))
        getLambdas(resultCandidate, discreteLines).foreach {
          lambdaList => resultLine = Some(resultCandidate.moveOrigin(lambdaList.min - 1))
        }
        j += 1
      }
      i += 1
    }


    resultLine
  }


  def getLambdas(resultCandidate: DiscreteLine, allLines: List[DiscreteLine]): Option[List[BigInt]] = {
    allLines.zipWithIndex.foldLeft(Option(List.empty[BigInt])) {
      case (Some(lambdaList), (newLine, index)) =>
        discreteLineIntersection(resultCandidate, newLine).map {
          case (targetLambda, _) =>
            lambdaList :+ targetLambda
        }
      case (None, _) =>
        None
    }
  }

  def getCandidatePoint(lineIndex: Int): (DecimalPoint, DecimalPoint) = {
    val firstLine = discreteLines(lineIndex)

    val vectors = discreteLines.indices.flatMap {
      i =>
        val secondLine = discreteLines(i)
        val vectorialProduct = firstLine.v.vectorialProduct(secondLine.v)
        if (vectorialProduct.squareNorm > 0) Some(DecimalVector.from(getDistance(firstLine, secondLine).get._2))
        else None
    }

    val sum = vectors.foldLeft(DecimalVector(0, 0, 0)) { case (accSum, newVector) => accSum + newVector }
    val mean = sum.byScalar(1/vectors.size.toDouble)


    val sumSq = vectors.foldLeft(DecimalVector(0,0,0)){
      case (accSumSq, newVector) =>
        accSumSq + DecimalVector(
          (newVector.x - mean.x) * (newVector.x - mean.x),
          (newVector.y - mean.y) * (newVector.y - mean.y),
          (newVector.z - mean.z) * (newVector.z - mean.z)
        )
      }

    val variance = sumSq.byScalar(1/(vectors.size -1).toDouble)
    (DecimalPoint(0,0,0) + mean, DecimalPoint(0,0,0) + variance)
  }

  def getCandidateLambda(lineIndex: Int): (BigInt, DecimalPoint) = {

    val (candidatePoint, variance) = getCandidatePoint(lineIndex)
    val firstLine = discreteLines(lineIndex)
    val coordPairs = List(
      (firstLine.pointAt(0).x, candidatePoint.x, firstLine.v.x),
      (firstLine.pointAt(0).y, candidatePoint.y, firstLine.v.y),
      (firstLine.pointAt(0).z, candidatePoint.z, firstLine.v.z),
    )

    val (lineCoord, candidCoord, vectorCoord) = coordPairs.find(_._3.abs > 0).get

    ((candidCoord.toBigInt - lineCoord) / vectorCoord, variance)

  }


  def getDistance(l1: DiscreteLine, l2: DiscreteLine): Option[(BigDecimal, DecimalPoint, DecimalPoint)] = Try{
    val l1d = l1.toDecimal
    val l2d = l2.toDecimal
    val aToB = DecimalVector.from(l1d.p) + DecimalVector.from(l2d.p).byScalar(-1)
    val vectProduct = l1d.v.vectorialProduct(l2d.v)

    val distance = Option.when(vectProduct.norm != 0)((vectProduct.scalarProduct(aToB) / vectProduct.norm).abs)
    val lambda1 = l2d.v.vectorialProduct(vectProduct).scalarProduct(DecimalVector.from(l1d.p, l2d.p)) / (vectProduct.scalarProduct(vectProduct))
    val lambda2 = l1d.v.vectorialProduct(vectProduct).scalarProduct(DecimalVector.from(l1d.p, l2d.p)) / (vectProduct.scalarProduct(vectProduct))

    distance.map(d => (d, l1d.pointAt(lambda1), l2d.pointAt(lambda2)))
  }.toOption.flatten

}
