package year_2024.day_005

object Main1 extends App {

  val (pageOrderingRules, updates) = Parser.readInput(isSample = false)

  println(updates.filter{
    update =>
      update.pages.zipWithIndex.forall {
        case (beforePage, index) =>
          update.pages.drop(index + 1).forall{
            pageAfter => pageOrderingRules.exists{
              pageOrderingRule =>
                pageOrderingRule.before == beforePage &&
                  pageOrderingRule.after == pageAfter
            }
          }
      }
  }.map(_.middle).sum)

}
