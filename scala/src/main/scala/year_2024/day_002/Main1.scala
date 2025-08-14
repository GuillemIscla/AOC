package year_2024.day_002

object Main1 extends App {

  val input = Parser.readInput(isSample = false)

  println(input.count{
    line =>
      line.zip(line.head :: line) match {
        case _ :: middle =>
          val minusList = middle.map{ case (a, b) => b - a}
          if(minusList.contains(0)) false
          else if(minusList.head < 0) minusList.forall{ elem => elem < 0 && elem > -4 }
          else minusList.forall{ elem => elem > 0 && elem < 4 }
      }
  })

}
