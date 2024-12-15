package year_2024.day_015

import scala.io.Source

object Parser {

  def readInput(isSample:Option[Int]):(Warehouse, List[RobotMovement]) = {
    if(isSample.contains(0)){
      parseInput("""##########
                   |#..O..O.O#
                   |#......O.#
                   |#.OO..O.O#
                   |#..O@..O.#
                   |#O#..O...#
                   |#O..O..O.#
                   |#.OO.O.OO#
                   |#....O...#
                   |##########
                   |
                   |<vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
                   |vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
                   |><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
                   |<<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
                   |^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
                   |^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
                   |>^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
                   |<><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
                   |^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
                   |v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^""".stripMargin.split("\n").toList)
    }
    else if (isSample.contains(1)) {
      parseInput("""########
                   |#..O.O.#
                   |##@.O..#
                   |#...O..#
                   |#.#.O..#
                   |#...O..#
                   |#......#
                   |########
                   |
                   |<^^>>>vv<v>>v<<""".stripMargin.split("\n").toList)
    }
    else if (isSample.contains(2)) {
      parseInput(
        """#######
          |#...#.#
          |#.....#
          |#..OO@#
          |#..O..#
          |#.....#
          |#######
          |
          |<vv<<^^<<^^""".stripMargin.split("\n").toList)
    }
    else {
      parseInput(Source.fromResource("year_2024/day_015_input.txt").getLines().toList)
    }
  }

  def parseInput(lines: List[String]):(Warehouse, List[RobotMovement]) = {
    val separation = lines.indexOf("")
    val warehouse =
      Warehouse(lines.take(separation).map{
          line => line.toList.map(WarehouseElement.fromChar)
      })

    val movements =
      lines.drop(separation + 1).mkString.map(RobotMovement.fromChar).toList

    (warehouse, movements)
  }


}
