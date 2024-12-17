package year_2023.day_025

object Main_1 extends App {
  val apparatus = Parser.readInput(isSample = false)

  val wires:List[(String, String)] =
    (for {
      component <- apparatus.components
      wire <- component.wired
    } yield {
      if(component.name < wire) (component.name, wire)
      else (wire, component.name)
    }).distinct


  println(apparatus.components.size)
  println(wires.size)

//  val wireTriplets:List[Set[(String, String)]] =
//    for {
//      wire1 <- wires
//      wire2 <- wires
//      if wire1 != wire2
//      wire3 <- wires
//      if wire1 != wire3 && wire2 != wire3
//    } yield Set(wire1, wire2, wire3)
//
//  println(wireTriplets.size)
//
//
//  val disconnectingTriplet =
//    wireTriplets.find {
//      wireTriplet =>
//        val apparatusDisconnected =
//          wireTriplet.foldLeft(apparatus){
//            case (apparatusAcc, wire) => apparatusAcc.disconnect(wire)
//          }
//        apparatusDisconnected.groups.size == 2
//    }.get
//
//  val apparatusDisconnected =
//    disconnectingTriplet.foldLeft(apparatus) {
//      case (apparatusAcc, wire) => apparatusAcc.disconnect(wire)
//    }
//
//  println(apparatusDisconnected.groups.map(_.size).product)

  println(apparatus.components.map(c => (c.name, c.wired.size)).mkString("\n"))

}
