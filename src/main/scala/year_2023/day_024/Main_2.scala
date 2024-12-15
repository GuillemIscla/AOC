package year_2023.day_024



/**
 * Mostly, it comes down to producing equations with the variables of the result line.
 * The result hailstone is (all unknown)
 *  (a_r, b_r, c_r) + l * (x_r, y_r, z_r)
 * This is the i-th hailstone (all defined):
 *  (a_i, b_i, c_i) + l * (x_i, y_i, z_i)
 * At l_i, the result line intersects with the i line and we get:
 *  a_i + l_i * x_i = a_r + l_i * x_r  --> l_i = (x_i - x_r) / (a_r - a_i)
 *  b_i + l_i * y_i = b_r + l_i * y_r  --> l_i = (y_i - y_r) / (b_r - b_i)
 * Equalling the two equations:
 *  (x_i - x_r) / (a_r - a_i) = (y_i - y_r) / (b_r - b_i) --> (x_r - x_i) * (b_r - b_i) - (y_r - y_i) * (a_r - a_i) = 0
 * Therefore:
 *  x_r * b_r + x_r * (-b_i) -x_i * b_r  + x_i * b_i -y_r * a_r + y_r * a_i + y_i * a_r - y_i * a_i = 0
 * Let's make a variable change:
 * alpha = x_r * b_r - y_r * a_r
 * Then we have:
 *  alpha * (1) + x_r * (-b_i) + b_r * (-x_i) + y_r * (a_i) + a_r * (y_i) = y_i * a_i - x_i * b_i
 *
 * Which is an equation with 5 variables: alpha, x_r, b_r, y_r and a_r.
 * We can make one equation by each Hailstone. We just need to care for the determinant not
 * being 0 when adding up another equation until we finally gather 5 of them.
 *
 * Once the system is resolved just use all these found values to find l_i for i=0 and i=1:
 *  a_i + l_i * x_i = a_r + l_r * x_r --> l_i = (a_r - a_i) / (x_i - x_r)
 * We just need to take care (x_i - x_r) is not zero but we can use the next coordinate as an alternative in those cases:
 *  b_i + l_i * y_i = b_r + l_r * y_r --> l_i = (b_r - b_i) / (y_i - y_r)
 *
 * Then we find the z coordinate where the result hailstone intersects with the i-th hailstone for i=0 and i=1:
 *  c_i + l_i * z_i = intersection_z_i
 *
 * And we build 2 x 2 system to find z_r and c_r:
 *  c_r * (1) + z_r * (l_i) = intersection_z_i
 * */
object Main_2 extends App {

  val hailstoneList = Parser.readInput(false)

  def buildEquation(hailstone: Hailstone):List[Double] = {
    List(1, -hailstone.initial.y, -hailstone.speed.x, hailstone.initial.x, hailstone.speed.y,
      hailstone.speed.y * hailstone.initial.x - hailstone.speed.x * hailstone.initial.y)
  }

  val matrix =
    hailstoneList.map(buildEquation).foldLeft(List.empty[List[Double]]) {
      case (accMatrix, _) if accMatrix.size == 5 => accMatrix
      case (accMatrix, newRow) =>
        val newMatrix = accMatrix :+ newRow
        if(AlgebraService.determinant(newMatrix).abs > 0.01) newMatrix
        else accMatrix
    }
  val Some(List(_, x_r, b_r, y_r, a_r)) = AlgebraService.gauss(matrix)
  val hailstone0 = hailstoneList.head
  val hailstone1 = hailstoneList(1)

  val l_0 =
    if(Math.abs(x_r - hailstone0.speed.x) > 0.01)
      (hailstone0.initial.x - a_r) / (x_r - hailstone0.speed.x)
    else
      (hailstone0.initial.y - b_r) / (y_r - hailstone0.speed.y)
  val l_1 =
    if (Math.abs(x_r - hailstone1.speed.x) > 0.01)
      (hailstone1.initial.x - a_r) / (x_r - hailstone1.speed.x)
    else
      (hailstone1.initial.y - b_r) / (y_r - hailstone1.speed.y)


  val intersection_z_0 = hailstone0.initial.z + l_0 * hailstone0.speed.z
  val intersection_z_1 = hailstone1.initial.z + l_1 * hailstone1.speed.z

  val Some(List(c_r, z_r)) =
    AlgebraService.gauss(List(
      List(1, l_0, intersection_z_0),
      List(1, l_1, intersection_z_1)
    ))


  println((a_r + b_r + c_r).toLong)

}
