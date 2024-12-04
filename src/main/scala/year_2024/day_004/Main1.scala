package year_2024.day_004


object Main1 extends App {

  val input = Parser.readInput(isSample = true)
  val inputIndexed = input.map(_.zipWithIndex).zipWithIndex

  println(inputIndexed.map{
    case (line, x_i) => line.map{
      case ('X', x_j) =>
        (for {
          (m_i, m_j) <- hasCharNear(inputIndexed, 'M', x_i, x_j, None)
          (a_i, a_j) <- hasCharNear(inputIndexed, 'A', m_i, m_j, Some((m_i - x_i, m_j - x_j)))
          (i_s, j_s) <- hasCharNear(inputIndexed, 'S', a_i, a_j, Some((m_i - x_i, m_j - x_j)))
        } yield ()).size
      case _ => 0
    }.sum
  }.sum)

  def hasCharNear(inputIndexed: List[(List[(Char, Int)], Int)], char:Char, char_i:Int, char_j:Int, direction:Option[(Int, Int)]):List[(Int, Int)] = {
    (-1 to 1).flatMap {
      i =>
        (-1 to 1).flatMap(
          j =>
            direction match {
              case Some((d_i, d_j)) =>
                if(i != d_i || j != d_j) None
                else if (char_i + i < 0 || char_i + i >= inputIndexed.size || char_j + j < 0 || char_j + j >= inputIndexed.size) None
                else if (inputIndexed(char_i + i)._1(char_j + j)._1 == char) Some((char_i + i, char_j + j))
                else None
              case None =>
                if (char_i + i < 0 || char_i + i >= inputIndexed.size || char_j + j < 0 || char_j + j >= inputIndexed.size) None
                else if (inputIndexed(char_i + i)._1(char_j + j)._1 == char) Some((char_i + i, char_j + j))
                else None
            }

        )
    }.toList
  }

}
