package year_2023.day_015

import scala.collection.mutable

case class LabelledLens(label:List[Char], power:Int)

case class LensBox(boxIndex:Int, lenses:List[LabelledLens]){
  val focusingPower:Int = lenses.zipWithIndex.foldLeft(0){
    case (aux, (newLens, lensIndex)) =>
      aux + ((boxIndex + 1) * (lensIndex + 1) * newLens.power)
  }
}


trait LensInstruction

case class AddLens(position:Int, labelledLens: LabelledLens) extends LensInstruction
case class RemoveLens(position:Int, label:List[Char]) extends LensInstruction

case class LensBoxLine(boxes:List[LensBox]){
  def processInstruction(lensInstruction: LensInstruction):LensBoxLine =
    lensInstruction match {
      case AddLens(position, labelledLens) =>
        val mutBoxes = mutable.ListBuffer.from(boxes)
        mutBoxes(position) =
          mutBoxes(position).lenses.find(_.label == labelledLens.label) match {
            case Some(foundLens) =>
              val mutLenses = mutable.ListBuffer.from(mutBoxes(position).lenses)
              mutLenses(mutLenses.indexOf(foundLens)) = labelledLens
              mutBoxes(position).copy(
                lenses = List.from(mutLenses)
              )
            case None =>
              mutBoxes(position).copy(
                lenses = mutBoxes(position).lenses :+ labelledLens
              )
          }
        LensBoxLine(List.from(mutBoxes))
      case RemoveLens(position, label) =>
        val mutBoxes = mutable.ListBuffer.from(boxes)

        mutBoxes(position) = mutBoxes(position).copy(
          lenses = mutBoxes(position).lenses.filter(_.label != label)
        )
        LensBoxLine(List.from(mutBoxes))
    }
}

object LensBoxLine {
  def empty():LensBoxLine = {
    LensBoxLine((0 until 256).map{
      boxIndex => LensBox(boxIndex, List.empty[LabelledLens])
    }.toList)
  }
}