package year_2024.day_023

object Main1 extends App {

  val (computers, links) = Parser.readInput(isSample = false)


  val computerSetsWithT =
    for {
      (a, indexA) <- computers.zipWithIndex
      (b, indexB) <- computers.zipWithIndex
      if indexB > indexA && links.exists(link => link.isLink(a, b))
      (c, indexC) <- computers.zipWithIndex
      if indexC > indexB && links.exists(link => link.isLink(b, c)) && links.exists(link => link.isLink(c, a)) && (a.startsWithT || b.startsWithT || c.startsWithT)
    } yield Set(a, b, c)

  println(computerSetsWithT)



}
