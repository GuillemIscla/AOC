package year_2023.day_009

case class History(singleValueHistory:List[Int]){
  val historySequences:List[List[Int]] = {
    var currentSequence = singleValueHistory
    var accHistorySequences = List(singleValueHistory)
    while(!currentSequence.forall(_ == 0)){
      val nextSequence = currentSequence.foldLeft((List.empty[Int], Option.empty[Int])){
        case ((accList, None), newHistoryValue) =>
          (accList, Some(newHistoryValue))
        case ((accList, Some(lastHistoryValue)), newHistoryValue) =>
          (accList :+ (newHistoryValue - lastHistoryValue), Some(newHistoryValue))
      }._1
      accHistorySequences = accHistorySequences :+ nextSequence
      currentSequence = nextSequence
    }
    accHistorySequences
  }
  def getPrediction:Int = historySequences.foldRight(0){
    case (historySequences, acc) => acc + historySequences.last
  }

  def getExtrapolation:Int = historySequences.foldRight(0) {
    case (historySequences, acc) => historySequences.head - acc
  }
}
