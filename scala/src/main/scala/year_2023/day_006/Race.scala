package year_2023.day_006

case class Race(time:Long, distance:Long){
  def numberOfWaysToWin:Long = (0 to time.toInt).map {
    i => i * (time - i)
  }.count(_ > distance)
}
