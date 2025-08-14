package year_2024.day_021

trait Key {
  def toChar:Char
}

case object Up extends Key {
  override def toChar: Char = '^'
}
case object Down extends Key {
  override def toChar: Char = 'v'
}
case object Left extends Key {
  override def toChar: Char = '<'
}
case object Right extends Key {
  override def toChar: Char = '>'
}
case object A extends Key {
  override def toChar: Char = 'A'
}

case class Number(raw:Int) extends Key {
  override def toChar: Char = raw.toString.head
}

object Key {
  def fromChar(raw:String):Key = allButNumber.find(_.toChar == raw.head).getOrElse(Number(raw.toInt))

  def allButNumber:List[Key] = List(Up, Down, Left, Right, A)

  def keyListToNumber(keyList: List[Key]): Int =
    keyList.map(_.toChar).foldLeft("") {
      case (acc, newChar) =>
        acc ++ newChar.toString.toIntOption.map(_.toString).getOrElse("")
    }.toIntOption.getOrElse(0)
}