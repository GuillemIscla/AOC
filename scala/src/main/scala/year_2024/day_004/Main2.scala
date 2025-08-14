package year_2024.day_004

object Main2 extends App {
  val input = Parser.readInput(isSample = false)
  val inputIndexed = input.map(_.zipWithIndex).zipWithIndex

  println(inputIndexed.map {
    case (line, m_i) => line.map {
      case ('M', m_j) =>
        val (h, v) = hasM(inputIndexed, m_i, m_j)
        val m_h =
          if(h){
            val (u, d) = hasA(inputIndexed, m_i, m_j, h = true)
            val m_h_u =
              if(u && hasSs(inputIndexed, m_i, m_j, h = true, u = true)) 1
              else 0

            val m_h_d =
              if (d && hasSs(inputIndexed, m_i, m_j, h = true, u = false)) 1
              else 0
            m_h_u + m_h_d
          }
          else 0
        val m_v =
          if (v) {
            val (u, d) = hasA(inputIndexed, m_i, m_j, h = false)
            val m_v_u =
              if (u && hasSs(inputIndexed, m_i, m_j, h = false, u = true)) 1
              else 0

            val m_v_d =
              if (d && hasSs(inputIndexed, m_i, m_j, h = false, u = false)) 1
              else 0
            m_v_u + m_v_d
          }
          else 0

        m_h + m_v
      case _ => 0
    }.sum
  }.sum)

  def hasM(inputIndexed: List[(List[(Char, Int)], Int)], i:Int, j:Int):(Boolean, Boolean) = {
    val h =
      if (j + 2 >= inputIndexed.size) false
      else inputIndexed(i)._1(j + 2)._1 == 'M'

    val v =
      if(i + 2 >= inputIndexed.size) false
      else inputIndexed(i + 2)._1(j)._1 == 'M'

    (h, v)
  }
  def hasA(inputIndexed: List[(List[(Char, Int)], Int)], i:Int, j:Int, h:Boolean):(Boolean, Boolean) = {
    val u =
      if(i + 1 >= inputIndexed.size || j + 1 >= inputIndexed.size) false
      else inputIndexed(i + 1)._1(j + 1)._1 == 'A'
    val d = {
      if(h){
        if (i - 1 < 0 || j + 1 >= inputIndexed.size) false
        else inputIndexed(i - 1)._1(j + 1)._1 == 'A'
      }
      else {
        if (i + 1 >= inputIndexed.size || j - 1 < 0) false
        else inputIndexed(i + 1)._1(j - 1)._1 == 'A'
      }
    }
    (u, d)
  }

  def hasSs(inputIndexed: List[(List[(Char, Int)], Int)], i:Int, j:Int, h:Boolean, u:Boolean):Boolean = {
    if (h && u) {
      if (i + 2 >= inputIndexed.size || j + 2 >= inputIndexed.size) false
      else inputIndexed(i + 2)._1(j)._1 == 'S' && inputIndexed(i + 2)._1(j + 2)._1 == 'S'
    }
    else if (h && !u) {
      if (i - 2 < 0 || j + 2 >= inputIndexed.size) false
      else inputIndexed(i - 2)._1(j)._1 == 'S' && inputIndexed(i - 2)._1(j + 2)._1 == 'S'
    }
    else if(!h && u){
      if (i + 2 >= inputIndexed.size || j + 2 >= inputIndexed.size) false
      else inputIndexed(i)._1(j + 2)._1 == 'S' && inputIndexed(i + 2)._1(j + 2)._1 == 'S'
    }
    else {
      if (i + 2 >= inputIndexed.size || j - 2 < 0) false
      else inputIndexed(i)._1(j - 2)._1 == 'S' && inputIndexed(i + 2)._1(j - 2)._1 == 'S'
    }
  }

}
