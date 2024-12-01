package year_2023.day_020

import scala.io.Source

object Parser {

  def readInput(isSample:Option[Int]):ModuleSet = {
    if (isSample.contains(0)) {
      parseModuleSet("""broadcaster -> a, b, c
                       |%a -> b
                       |%b -> c
                       |%c -> inv
                       |&inv -> a""".stripMargin.split("\n").toList)
    }
    else if(isSample.contains(1)){
      parseModuleSet(
        """broadcaster -> a
          |%a -> inv, con
          |&inv -> b
          |%b -> con
          |&con -> output""".stripMargin.split("\n").toList)
    }
    else parseModuleSet(Source.fromResource("year_2023/day_020_input.txt").getLines().toList)
  }



  def parseModuleSet(lines:List[String]):ModuleSet = {
    val modulesAndReceivers = lines.map(parseModules)
    val receiversRaw = modulesAndReceivers.flatMap(_._2)
    val outputs =
      receiversRaw
        .filterNot(receiverRaw => lines.map(parseModules).map(_._1.name).contains(receiverRaw))
        .map(name => (Output(name), List.empty))
    ModuleSet(modulesAndReceivers ++ outputs)
  }


  def parseModules(line: String): (Module, List[String]) = {
    val moduleRegex = "([%&a-z]+) -> ([a-z, ]+)".r
    val result = moduleRegex.findFirstMatchIn(line).getOrElse(throw new Exception(s"Cannot parse module with line '$line'"))
    val modules = result.group(1)
    (Module.fromRaw(result.group(1)), result.group(2).split(", ").toList)
  }







}
