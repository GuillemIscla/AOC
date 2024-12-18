package year_2024.day_017


object Main2 extends App {

  val threeBitProgram = Parser.readInput(None)

  println(toLong(findRegisterA(List.empty).head))

  def findRegisterA(firstOctids:List[Int]):List[List[Int]] = {
    if(firstOctids.size == threeBitProgram.intBand.size) List(firstOctids)
    else {
      (0 until 8).flatMap{
        i =>
          val (_, output) = setRegisterA(firstOctids :+ i).execute()
          if(output.map(_.toInt) == threeBitProgram.intBand.takeRight(firstOctids.size + 1)) {
            Some(firstOctids :+ i)
          }
          else None

      }.flatMap(findRegisterA).toList
    }
  }

  def toLong(octal:List[Int]):Long = {
    octal.reverse.foldLeft((0L, 1L)) {
      case ((acc, pow), octid) => (acc + octid * pow, pow * 8)
    }._1
  }

  def toList(long: Long):List[Int] = {
    var accInt = long
    var result = List.empty[Int]
    while(accInt > 0) {
      result = (accInt % 8).toInt :: result
      accInt /= 8
    }
    result
  }

  def setRegisterA(newRegisterAList:List[Int]):ThreeBitProgram = {
    val newRegisterA = toLong(newRegisterAList)
    val newRegisters = threeBitProgram.registers.copy(registerA = newRegisterA)
    threeBitProgram.copy(registers = newRegisters)
  }
}
