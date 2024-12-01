package year_2023.day_024

import AlgebraService._
case class Point(x:BigInt, y:BigInt, z:BigInt) {
  def +(v:Vector):Point = Point(x + v.x, y + v.y, z + v.z)

  def vectorFromOrigin:Vector = Vector(x,y,z)

  def toDecimal:DecimalPoint = DecimalPoint(BigDecimal(x), BigDecimal(y), BigDecimal(z))
}
case class Vector(x:BigInt, y:BigInt, z:BigInt) {
  def byScalar(k:BigInt):Vector = Vector(x * k, y * k, z * k)
  
  def normalize():Vector = {
    val vGcd:BigInt = gcd(gcd(x, y),z)
    Vector(x/vGcd, y/vGcd, z/vGcd)
  }
  def +(v:Vector):Vector = Vector(x + v.x, y + v.y, z + v.z)

  def vectorialProduct(other: Vector): Vector = Vector(
    y * other.z - z * other.y,
    z * other.x - x * other.z,
    x * other.y - y * other.x
  )

  def scalarProduct(other: Vector): BigInt = x * other.x + y * other.y + z * other.z

  def squareNorm:BigInt = x*x + y*y + z*z

  def toDecimal:DecimalVector = DecimalVector(BigDecimal(x), BigDecimal(y), BigDecimal(z))
  def isNull:Boolean = x == 0 && y == 0 && z == 0
}

object Vector {
  def from(p1: Point, p2: Point): Vector = Vector(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z)

  def from(p1: Point): Vector = Vector.from(Point(0,0,0), p1)

}
case class DiscreteLine(p:Point, v:Vector) {

  assert(!v.isNull)
  def moveOrigin(displacement:BigInt):DiscreteLine = DiscreteLine(p + v.byScalar(displacement), v)

  def pointAt(lambda:BigInt):Point = {
    val vv = v.byScalar(lambda)
    val result = p + vv
    result
  }

  def toDecimal:DecimalLine = DecimalLine(p.toDecimal, v.toDecimal)
}

object DiscreteLine {
  def from(p1:Point, p2:Point):DiscreteLine = DiscreteLine(p1, Vector.from(p1, p2).normalize())
}


case class DecimalPoint(x:BigDecimal, y:BigDecimal, z:BigDecimal) {
  def +(v:DecimalVector):DecimalPoint = DecimalPoint(x + v.x, y + v.y, z + v.z)

  def vectorFromOrigin:DecimalVector = DecimalVector(x,y,z)
  def toDiscrete:Point = Point(x.toLong, y.toLong, z.toLong)
}
case class DecimalVector(x:BigDecimal, y:BigDecimal, z:BigDecimal) {
  def +(v: DecimalVector): DecimalVector = DecimalVector(x + v.x, y + v.y, z + v.z)

  def vectorialProduct(other: DecimalVector): DecimalVector = DecimalVector(
    y * other.z - z * other.y,
    z * other.x - x * other.z,
    x * other.y - y * other.x
  )

  def scalarProduct(other: DecimalVector): BigDecimal = x * other.x + y * other.y + z * other.z

  def byScalar(k: BigDecimal): DecimalVector = DecimalVector(x * k, y * k, z * k)

  def squareNorm:BigDecimal = x*x + y*y + z*z

  def toDiscrete:Vector = Vector(x.toLong, y.toLong, z.toLong)

  def norm:BigDecimal = BigDecimal(Math.sqrt(squareNorm.toDouble))
}

object DecimalVector {
  def from(p1: DecimalPoint, p2: DecimalPoint): DecimalVector = DecimalVector(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z)

  def from(p1: DecimalPoint): DecimalVector = DecimalVector.from(DecimalPoint(0,0,0), p1)

}

case class DecimalLine(p:DecimalPoint, v:DecimalVector) {
  def pointAt(lambda: BigDecimal): DecimalPoint = p + v.byScalar(lambda)
}