package year_2024.day_014

object Main2 extends App {

  val input = Parser.readInput(isSample = false)
  var robots = input

  (0 to 10000).foreach {
    i =>
      val lotsOfX = robots.groupBy {
        br => br.px
      }.exists(_._2.size > 20)
      val lotsOfY = robots.groupBy {
        br => br.py
      }.exists(_._2.size > 20)
      if(lotsOfX && lotsOfY) {
        println(i)
        printRobots(robots)
      }
      robots = robots.map(_.move(1))
  }

  def printRobots(robots:List[BathroomRobot]):Unit = {
    val (areaWidth, areaHeight) = (robots.head.areaWidth, robots.head.areaHeight)

    (0 until areaHeight).foreach{
      j =>
        (0 until areaWidth).foreach{
          i =>
            val count = robots.count(r => r.px == i && r.py == j)
             print{
                if(count > 9) 'X'
                else if (count == 0) '.'
                else count
              }
        }
        println()
    }
  }

}
