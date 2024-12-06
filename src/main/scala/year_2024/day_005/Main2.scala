package year_2024.day_005

object Main2 extends App {
  val (pageOrderingRules, updates) = Parser.readInput(isSample = false)

  val wrongUpdates = updates.filter {
    update =>
      !update.pages.zipWithIndex.forall {
        case (beforePage, index) =>
          update.pages.drop(index + 1).forall {
            pageAfter =>
              pageOrderingRules.exists {
                pageOrderingRule =>
                  pageOrderingRule.before == beforePage &&
                    pageOrderingRule.after == pageAfter
              }
          }
      }
  }

  println(wrongUpdates.map{
    update =>
      Update(update.pages.foldLeft((List.empty[Int], update.pages)){
        case ((correct, partialIncorrect), _) =>
          val (first, remaining) = getFirst(partialIncorrect)
          (correct :+ first, remaining)
      }._1)
  }.map(_.middle).sum)


  def getFirst(partialUpdate:List[Int]):(Int, List[Int]) = {
    val first = partialUpdate.zipWithIndex.find{
      case (beforePage, index) =>
        partialUpdate.drop(index + 1).forall{
          pageAfter => pageOrderingRules.exists{
            pageOrderingRule =>
              pageOrderingRule.before == beforePage &&
                pageOrderingRule.after == pageAfter
          }
        }
    }.head._1

    val index = partialUpdate.indexWhere(_ == first)
    val remaining = partialUpdate.take(index) ++ partialUpdate.drop(index + 1)
    (first, remaining)
  }

}
