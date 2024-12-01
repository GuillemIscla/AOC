package year_2023.day_024


import AlgebraService._

import scala.util.Try

/**
 * It all comes down to producing equations with the variables of the result line.
 * The result line is:
 * - p_r + l*v_r (vector notation)
 * - (p_r_x, p_r_y, p_r_z) + l * (v_r_x, v_r_y, v_r_z)
 *
 * At time l_1 hits the first line
 * p_r + l_1 * v_r = p_1 + l_1 * v_1
 *
 * Lets consider coordinates x and y for this equation and isolate l_1 on each:
 * l1 = (p_r_x - p_1_x) / (v_1_x - v_r_x)
 * l1 = (p_r_y - p_1_y) / (v_1_y - v_r_y)
 *
 * Let's keep in mind that all in _r_ are variables. Let's make substitution of l1 and operate:
 * (p_r_x - p_1_x) * (p_r_y - p_1_y) = (v_1_x - v_r_x) * (v_1_y - v_r_y)
 * p_r_x * p_r_y - v_r_x * v_r_y = p_r_x * p_1_y + p_r_y * p_1_x - p_1_x * p_1_y + v_1_x * v_1_y - v_1_x * v_r_y - v_1_y * v_r_x
 * The left part of this equation is non-dependent of the terms of line 1.
 * The right part, is a linear equation of 4 variables on terms of the result line:
 * We can produce the same equation for line 2:
 * p_r_x * p_r_y - v_r_x * v_r_y = p_r_x * p_2_y + p_r_y * p_2_x - p_2_x * p_2_y + v_2_x * v_2_y - v_2_x * v_r_y - v_2_y * v_r_x
 * The, when we make substitution on the left part, we get an equation with this terms:
 * variable ---> term
 * p_r_x ---------> p_2_y - p_1_y
 * p_r_y ---------> p_2_x - p_1_x
 * v_r_x ---------> v_1_y - v_2_y
 * v_r_y ---------> v_1_x - v_2_x
 * independent ---> v_2_x * v_2_y - p_2_x * p_2_y - v_1_x * v_1_y + p_1_x * p_1_y
 *
 * This equation has been inferred from lines 1 and 2. For each i = 1,2,3,4 we consider i and i+1 to get
 * independent equations. We have a 4 x 4 system. So we obtain values for p_r_x, p_r_y, v_r_x and v_r_y
 *
 * We find lambda_1 and lambda_2 with the equation2:
 * p_r_x + l_1 * v_r_x = p_1_x + l_1 * v_1_x
 * p_r_x + l_2 * v_r_x = p_2_x + l_2 * v_2_x
 *
 * And then we consider the equations:
 * p_r_z = p_1_z + l_1 * v_1_z - l_1 * v_r_z
 * p_r_z = p_2_z + l_2 * v_2_z - l_2 * v_r_z
 *
 * We make substitution on the left side and we get a linear equation on v_r_z:
 * v_r_z = (p_1_z + l_1 * v_1_z - p_2_z - l_2 * v_2_z) / (l_1 - l_2)
 *
 * Then the next equation gives is p_r_x
 * p_r_x + l_1 * v_r_z = p_1_x + l_1 * v_1_x
 *
 *
 *
 *
 * */
object Main_2 extends App {


}
