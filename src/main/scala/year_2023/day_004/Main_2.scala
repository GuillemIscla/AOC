package year_2023.day_004

object Main_2 extends App {
  val originalScratchCards = Parser.readInput(isSample = false)

  val scratchCardWithCopies =
    originalScratchCards.indices.foldLeft(originalScratchCards){
      case (accScratchCards, newScratchCardIndex) =>
        val newScratchCard = accScratchCards(newScratchCardIndex)
        val newCopies = newScratchCard.actualWinningNumbers.size
        val mutableList = scala.collection.mutable.Seq.from(accScratchCards)
        (1 until newCopies + 1).foreach{
          i =>
            val toAddCopies = mutableList(newScratchCardIndex + i)
            mutableList(newScratchCardIndex + i) = toAddCopies.copy(copies = toAddCopies.copies + newScratchCard.copies)
        }
        List.from(mutableList)
    }

  println(scratchCardWithCopies.map(_.copies).sum)

}
