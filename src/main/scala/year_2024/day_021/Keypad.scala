package year_2024.day_021

import year_2024.day_021.Keypad.memoize

import scala.collection.mutable


trait Keypad {
  def dial(input: List[Key]): List[List[Key]] = {
    input.foldLeft((A:Key, List(List.empty[Key]))){
      case ((initialKey, accList), finalKey) =>
        val dialedList = dialOne(initialKey, finalKey)
        (finalKey, for {
          dialed <- dialedList
          acc <- accList
        } yield acc ++ dialed)
    }._2.distinct
  }

  protected val distribution:Map[Key, (Int, Int)]

  protected val forbidden: (Int, Int)

  def dialOne(origin: Key, destination: Key): List[List[Key]] = {
    val (x_1, y_1) = distribution(origin)
    val (x_2, y_2) = distribution(destination)
    val (x_3, y_3) = (x_1 - x_2, y_1 - y_2)
    val horizontal: List[Key] = List.fill(Math.abs(y_3))(if (y_3 > 0) Left else Right)
    val vertical: List[Key] = List.fill(Math.abs(x_3))(if (x_3 > 0) Up else Down)

    List(horizontal ++ vertical :+ A, vertical ++ horizontal :+ A)
      .filter(isValid(origin)).distinct

  }

  def isValid(origin: Key)(path: List[Key]): Boolean = {
    def keyToOffset(key:Key):(Int, Int) = key match {
      case Up => (-1, 0)
      case Down => (1, 0)
      case Left => (0, -1)
      case Right => (0, 1)
      case _ => (0, 0)
    }
    path.foldLeft((distribution(origin), true)){
      case ((position, false), _) => (position, false)
      case ((position, true), newKey) =>
        val (offset_x, offset_y) = keyToOffset(newKey)
        val newPosition = (position._1 + offset_x, position._2 + offset_y)
        (newPosition, newPosition != forbidden)
    }._2
  }



  def dialListCount(list: List[Key], deep: Int): Long = {
    if (deep == 0) list.size
    else {
      list.foldLeft((A:Key, 0L)) {
        case ((previous, count), next) =>
          (next, count + dialSingleCount(previous, next, deep))
      }._2
    }
  }

  def dialSingleCount(origin: Key, destination: Key, deep: Int): Long = {
    memoize.get(origin, destination, deep) match {
      case Some(value) => value
      case None =>
        dialOne(origin, destination) match {
          case head :: Nil =>
            val result = dialListCount(head, deep - 1)
            memoize.addOne((origin, destination, deep) -> result)
            result
          case head :: second :: Nil =>
            val result1 = dialListCount(head, deep - 1)
            val result2 = dialListCount(second, deep - 1)
            val result = Math.min(result1, result2)
            memoize.addOne((origin, destination, deep) -> result)
            result
        }
    }
  }

}

object Keypad {
  val memoize:mutable.HashMap[(Key, Key, Int), Long] =  new mutable.HashMap()
}


/**
 *
 * +---+---+---+
 * | 7 | 8 | 9 |
 * +---+---+---+
 * | 4 | 5 | 6 |
 * +---+---+---+
 * | 1 | 2 | 3 |
 * +---+---+---+
 *     | 0 | A |
 *     +---+---+
 * */
object NumericKeypad extends Keypad {

  protected val distribution:Map[Key, (Int, Int)] = {
    Map(
      Number(7) -> (0, 0),
      Number(8) -> (0, 1),
      Number(9) -> (0, 2),
      Number(4) -> (1, 0),
      Number(5) -> (1, 1),
      Number(6) -> (1, 2),
      Number(1) -> (2, 0),
      Number(2) -> (2, 1),
      Number(3) -> (2, 2),
      Number(0) -> (3, 1),
      A -> (3, 2)
    )
  }

  override val forbidden: (Int, Int) = (3, 0)

}


/**
 *      +---+---+
 *      | ^ | A |
 * +---+---+---+
 * | < | v | > |
 * +---+---+---+
 **/

object DirectionKeypad extends Keypad {
  val distribution:Map[Key, (Int, Int)] = {
    Map(
      Up -> (0, 1),
      A -> (0, 2),
      Left -> (1, 0),
      Down -> (1, 1),
      Right -> (1, 2)
    )
  }
  val forbidden:(Int, Int) = (0,0)

}

