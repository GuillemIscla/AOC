package year_2023.day_024

case class Rational(a:BigInt, b:BigInt){
  assert(b != 0)
  def normalize():Rational = {
    val myGcd = AlgebraService.gcd(a,b)
    Rational(a/myGcd, b/myGcd)
  }

  def product(other: Rational):Rational = Rational(a * other.a, b * other.b).normalize()

  def product(other: BigInt):Rational = product(Rational(other, 1)).normalize()

  def divide(other:Rational):Rational = product(Rational(other.b, other.a)).normalize()

  def divide(other:BigInt):Rational = divide(Rational(other, 1)).normalize()

  def plus(other:Rational):Rational = {
    Rational(a * other.b + b * other.a, b * other.b).normalize()
  }

  def plus(other:BigInt):Rational = plus(Rational(other, 1))

  def isInt:Boolean = normalize().b == 1

  def value:BigDecimal = BigDecimal(a) / BigDecimal(b)

}
