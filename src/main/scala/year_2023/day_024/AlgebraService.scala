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

  def gauss(matrix:List[List[Double]]):Option[List[Double]] = {
    def triangulate(upper:List[Double], subMatrix:List[List[Double]], column:Int):List[List[Double]] = {
      subMatrix.indices.foldLeft(subMatrix){
        case (accSubMatrix, i) =>
          val k =  -subMatrix(i)(column) / upper(column)
          val newRow = subMatrix(i).zipWithIndex.map{
            case (value, j) => value + upper(j) * k
          }
          (accSubMatrix.take(i) :+ newRow) ++ accSubMatrix.drop(i + 1)
      }
    }
    def addSolution(triangularMatrix:List[List[Double]])(foundSolutions:List[Double]):Option[List[Double]] = {
      val row = triangularMatrix.size - foundSolutions.size -1
      val sumProd = {
        triangularMatrix(row).dropRight(1).reverse.zip(foundSolutions.reverse).map{
          case (a, b) => a * b
        }.sum
      }
      val q = triangularMatrix(row)(triangularMatrix(row).size - foundSolutions.size -2)
      val c = triangularMatrix(row).last
      if(q == 0) None
      else Some(((c - sumProd) / q) :: foundSolutions)
    }

    val triangularMatrix = {
      (0 until matrix.size - 1).foldLeft(matrix){
        case (accMatrix, i) =>
          val newSubMatrix = triangulate(accMatrix(i), accMatrix.drop(i + 1), i)
          accMatrix.take(i + 1) ++ newSubMatrix
      }
    }

    matrix.indices.foldLeft(Option(List.empty[Double])){
      case (Some(accSolutions), _) => addSolution(triangularMatrix)(accSolutions)
      case _ => None
    }
  }

  def determinant(matrix:List[List[Double]]):Double = {
    val size = Math.min(matrix.size, matrix.head.size)
    val squareMatrix = matrix.map(_.take(size))
    if(size == 1) squareMatrix.head.head

    else {
      (0 until size).map {
        i =>
          determinant(squareMatrix.tail.map {
            row => row.take(i) ++ row.drop(i + 1)
          }) * matrix.head(i) * (if (i % 2 == 0) 1 else -1)
      }.sum
    }
  }

}
