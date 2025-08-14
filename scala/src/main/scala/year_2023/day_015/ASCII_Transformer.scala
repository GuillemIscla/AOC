package year_2023.day_015

object ASCII_Transformer {

  def transformLabel(seq:List[Char]):Int =
    seq.foldLeft(0){
      case (acc, newChar) =>
        ((acc + newChar.toInt) * 17) % 256
    }

  def transformInstruction(seq:List[Char]):LensInstruction = {
    val instructionRegex = "([a-z]+)(\\-|=[0-9]+)".r
    instructionRegex.findFirstMatchIn(seq.mkString) match {
      case Some(removeResult) if removeResult.group(2) == "-" =>
        val label = removeResult.group(1).toList
        val position = transformLabel(label)
        RemoveLens(position, label)
      case Some(addResult) =>
        val label = addResult.group(1).toList
        val power = addResult.group(2).toList.tail.mkString.toInt
        val position = transformLabel(label)
        AddLens(position, LabelledLens(label, power))
      case None => throw new Exception(s"Could not transform instruction '${seq.mkString}'")
    }
  }


}
