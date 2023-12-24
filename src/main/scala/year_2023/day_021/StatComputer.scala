package year_2023.day_021


import java.lang.Math.abs

case class Stat(mapCoordinate: (Int, Int), startIteration: Int, finishIteration: Int, startCoordinate: (Int, Int))

case class WhenFilled(odd:Int, even:Int){
  def switch(doSwitch:Boolean):WhenFilled = if(doSwitch) WhenFilled(even, odd) else this
}

case class StatComputer(garden:Garden, rawStats: List[Stat]) {
  val validStats: List[Stat] = rawStats.filter(_.finishIteration > 0)

  val records: WhenFilled = WhenFilled(odd = 7481, even = 7407)

  def mapStartsFormula(map_Type: Map_Type): (Int, Int) = {
    def collectIteration(mapCoord:(Int, Int)):Int = {
      validStats.collectFirst {
        case Stat(thisMapCoord, startIteration, _, _) if thisMapCoord == mapCoord => startIteration
      }.get
    }

    (map_Type.isOrigin, map_Type.isAxis) match {
      case (true, _) => (0, 1)
      case (_, true) =>
        val firstStarts = collectIteration(map_Type.sample1)
        val secondStarts = collectIteration(map_Type.sample2)
        (2 * firstStarts - secondStarts, secondStarts - firstStarts)
      case _ =>
        val firstStarts = collectIteration(map_Type.sample1)
        val secondStarts = collectIteration(map_Type.sample2)
        (3 * firstStarts - 2 * secondStarts, secondStarts - firstStarts)
    }
  }

  def checkMapStartsFormula():Unit = {
    Map_Type.all.foreach{
      map_Type: Map_Type =>
        val (m, n) = mapStartsFormula(map_Type)
        validStats.collect {
          case Stat((map_x, map_y), startIteration, _, _) if map_Type.isThisType(map_x, map_y) => (map_x, map_y, startIteration)
        }.foreach {
          case (map_x, map_y, total) =>
            assert((abs(map_x) + abs(map_y)) * n + m == total, s"(abs(map_x) + abs(map_y)) * n + m == total, map_x=$map_x, map_y=$map_y, n=$n, m=$m, total=$total")
        }

    }
  }

  def mapTakesToFinish(map_Type: Map_Type): Int = {
    validStats.collectFirst {
      case Stat(thisMapCoord, startIteration, finishIteration, _) if thisMapCoord == map_Type.sample1 => finishIteration - startIteration
    }.get
  }
  def checkMapTakesToFinish(): Unit = {
    Map_Type.all.foreach {
      map_Type: Map_Type =>
        val ttf = mapTakesToFinish(map_Type)
        validStats.collect {
          case Stat((map_x, map_y), startIteration, finishIteration, _) if map_Type.isThisType(map_x, map_y) => (map_x, map_y, finishIteration - startIteration)
        }.foreach {
          case (_, _, total) =>
            assert(ttf == total, s"In $map_Type, ttf == total, ttf=$ttf, total=$total")
        }
    }
  }

  def whichCoordinatesStartsBeingFilled(map_Type: Map_Type):(Int, Int) = {
    validStats.collectFirst {
      case Stat(thisMapCoord, _, _, startCoordinate) if Map_Type.fromMap(thisMapCoord) == map_Type => startCoordinate
    }.get
  }

  def checkWhichCoordinatesStartsBeingFilled(): Unit = {
    Map_Type.all.foreach {
      map_Type: Map_Type =>
        val ccsbf = whichCoordinatesStartsBeingFilled(map_Type)
        validStats.collect {
          case Stat((map_x, map_y), _, _, startCoordinate) if map_Type.isThisType(map_x, map_y) => (map_x, map_y, startCoordinate)
        }.foreach {
          case (_, _, total) =>
            assert(ccsbf == total, s"In $map_Type, ccsbf == total, ccsbf=$ccsbf, total=$total")
        }
    }
  }

  def mapsStartedNotFinished(totalSteps:Long):List[Stat] = {
    Map_Type.all.flatMap {
      case X_0_Y_0 =>
        val originStat = validStats.find(_.mapCoordinate == (0,0)).get
        if(originStat.finishIteration > totalSteps)
          List(originStat.copy(finishIteration = -1))
        else {
          List.empty
        }
      case map_Type: Map_Type if map_Type.isAxis =>
        val (m, n) = mapStartsFormula(map_Type)
        val takesToFinish = mapTakesToFinish(map_Type)
        val radius = if ((totalSteps - m) < n) 0 else (totalSteps - m) / n
        val mapCoordinates = map_Type.mapsCoordinatesAtRadius(radius.toInt)
        val startCoordinate = whichCoordinatesStartsBeingFilled(map_Type)
        mapCoordinates.flatMap {
          mc =>
            val startIteration = (abs(mc._1) + abs(mc._2)) * n + m
            if (startIteration + takesToFinish <= totalSteps) None
            else Some(Stat(mc, startIteration, -1, startCoordinate))
        }

      case map_Type: Map_Type =>
        val (m, n) = mapStartsFormula(map_Type)
        val takesToFinish = mapTakesToFinish(map_Type)
        //(abs(map_x) + abs(map_y)) * n + m == total
        val radius = if ((totalSteps - m) < 2 * n) 0 else (totalSteps - m) / n
        val mapCoordinates = map_Type.mapsCoordinatesAtRadius(radius.toInt)
        val startCoordinate = whichCoordinatesStartsBeingFilled(map_Type)
        mapCoordinates.flatMap {
          mc =>
            val startIteration = (abs(mc._1) + abs(mc._2)) * n + m
            if(startIteration + takesToFinish <= totalSteps) None
            else Some(Stat(mc, startIteration, -1, startCoordinate))
        }
    }
  }


  def mapsFinishedOnOdd(totalSteps:Long):Long = {
    Map_Type.all.map {
      //abs(map_x) + abs(map_y)) * n + m == total
      case X_0_Y_0 => if(validStats.find(_.mapCoordinate == (0,0)).get.finishIteration > totalSteps) 0 else 1
      case map_Type: Map_Type =>

        val (m, n) = mapStartsFormula(map_Type)
        val takesToFinish = mapTakesToFinish(map_Type)
        val formulaTotal = (totalSteps - m - takesToFinish) / n
        map_Type.waysToSum(formulaTotal, isEven = false)
    }.sum
  }

  def mapsFinishedOnEven(totalSteps:Long):Long = {
    Map_Type.all.map {
      //abs(map_x) + abs(map_y)) * n + m == total
      case X_0_Y_0 => 0
      case map_Type: Map_Type =>
        val (m, n) = mapStartsFormula(map_Type)
        val takesToFinish =mapTakesToFinish(map_Type)
        val formulaTotal = (totalSteps - m - takesToFinish) / n
        map_Type.waysToSum(formulaTotal, isEven = true)
    }.sum
  }

  def checkMapsStartedNotFinished():Unit = {
    def collectStat(mapCoord: (Int, Int)): Stat = {
      validStats.collectFirst {
        case stat if stat.mapCoordinate == mapCoord => stat
      }.get
    }
    val toCheck = (-2 to 2).flatMap {
      i =>
        val zeroOne = collectStat((0,1))
        val zeroTwo = collectStat((0,2))
        val axisM = zeroOne.startIteration
        val axisN = zeroTwo.startIteration - axisM

        val oneOne = collectStat((1, 1))
        val oneTwo = collectStat((1, 2))
        val nonAxisM = oneOne.startIteration
        val nonAxisN = oneTwo.startIteration - nonAxisM

        List(0, 1, 2).map(j => axisM + axisN * j)
        List(0, 1, 2).map(j => nonAxisM + nonAxisN * j)
    }.toList ++ List(1,2,3)

    toCheck.foreach{
      iteration =>
        if(iteration == 394){
          println()
        }
        val formula = mapsStartedNotFinished(iteration).toSet
        val real = validStats.filter(s => s.startIteration <= iteration && s.finishIteration > iteration).map(_.copy(finishIteration = -1)).toSet
        assert(formula == real, s"Checking maps at iteration $iteration, formula == real, formula=$formula, real=$real, formula -- real=${formula -- real}, real -- formula=${real -- formula}")
    }
  }

  def allChecks(): Unit = {
    checkMapStartsFormula()
    checkMapTakesToFinish()
    checkWhichCoordinatesStartsBeingFilled()
    checkMapsStartedNotFinished()
  }

  def result(stepsLeftToday:Long):Long = {
    val notFinishedMapsResult =
      mapsStartedNotFinished(stepsLeftToday)
        .groupBy(s => (s.startIteration, s.startCoordinate)).view.mapValues(_.size)
        .map {
          case ((startIteration, (x, y)), repeat) =>
            repeat *
              Iterator.iterateClosed(
                Coordinate(x, y, 0, 0),
                garden,
                stepsLeftToday - startIteration)
        }.sum


    mapsFinishedOnEven(stepsLeftToday) * records.switch(stepsLeftToday % 2 == 1).even +
      mapsFinishedOnOdd(stepsLeftToday) * records.switch(stepsLeftToday % 2 == 1).odd +
      notFinishedMapsResult
  }
}