package year_2024.day_022


case class SecretIterating(secret: Long, prices: List[Int]){
  def iterate():SecretIterating = {
    val newSecret = Secret.allSteps(secret)
    if(prices.size < 5) SecretIterating(newSecret, prices :+ (newSecret % 10).toInt)
    else SecretIterating(newSecret, prices.drop(1) :+ (newSecret % 10).toInt)
  }
  def getPattern:List[Int] = {
    prices.tail.foldLeft((prices.head, List.empty[Int])) {
      case ((previous, diff), currentSecret) => (currentSecret, diff :+ (currentSecret - previous))
    }._2
  }
}

object Secret {

  def getIterator(secret:Long):SecretIterating = SecretIterating(secret, List((secret % 10).toInt))


  def step1(input: Long): Long = prune(mix(input, input * 64))

  def step2(input: Long): Long = prune(mix(input, input / 32))

  def step3(input: Long): Long = prune(mix(input, input * 2048))

  def allSteps(input:Long):Long =
    step3(step2(step1(input)))



  def mix(a: Long, b: Long): Long = {
    val aCoded = toBase2(a)
    val bCoded = toBase2(b)
    val aPadded = List.fill(bCoded.size - aCoded.size)(false) ++ aCoded
    val bPadded = List.fill(aCoded.size - bCoded.size)(false) ++ bCoded
    fromBase2(aPadded.zip(bPadded).map {
      case (true, true) => false
      case (false, false) => false
      case _ => true
    })
  }

  def toBase2(int: BigInt): List[Boolean] = {
    var accInt = int
    var result = List.empty[Boolean]
    while (accInt > 0) {
      result = (accInt % 2 == 1) :: result
      accInt /= 2
    }
    result
  }

  def fromBase2(base2: List[Boolean]): Long = {
    var accInt: Long = 0L
    var i = 0
    var pow = 1L
    while (base2.size > i) {
      accInt += {
        if (base2.reverse(i)) pow else 0L
      }
      i += 1
      pow *= 2
    }
    accInt
  }

  def prune(long: Long): Long = long % 16777216L

}
