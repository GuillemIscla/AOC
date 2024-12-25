package year_2024.day_023


//cwdyefiwjijvkaobqvryuawtxz not the right answer


object Main2 extends App {

  val (computers, links) = Parser.readInput(isSample = false)


//  //This code shows that all computers have exactly 13 links.
  //Tried to find sets of 13 interconnected computers
//  computers.map{
//    case Computer(name) =>
//      (name, links.count(link => link.a == name || link.b == name))
//  }.mkString("\n")

  val lanParty:Set[Computer] =
    computers.find(isInLANParty).map(getBigSetOfComputers).get

  println(lanParty.toList.map(_.name).sorted.mkString(","))


  def isInLANParty(c:Computer):Boolean =
    getBigSetOfComputers(c).size == 13


  def getBigSetOfComputers(c:Computer):Set[Computer] = {
    val computersLinked:Set[Computer] = getComputersLinked(c)
    computersLinked.map{
      computerToExclude =>
        val computersRemaining =  computersLinked - computerToExclude
        computersRemaining.foldLeft(computersRemaining + c) {
          case (accConnections, newComputer) =>
            accConnections.intersect(getComputersLinked(newComputer) + newComputer)
        }
    }.maxBy(_.size)
  }

  def getComputersLinked(c:Computer):Set[Computer] = {
    links.filter(l => l.has(c)).map{
      case ComputerLink(a, b) =>
        Computer(if (a == c.name) b else a)
    }.toSet
  }

}
