package year_2023.day_004

case class ScratchCard(id:Int, winningNumbers:List[Int], actualNumbers:List[Int], copies:Int = 1) {
  def actualWinningNumbers:List[Int] = winningNumbers.intersect(actualNumbers)
  def value:Int = Math.pow(2, actualWinningNumbers.length -1).toInt
}
