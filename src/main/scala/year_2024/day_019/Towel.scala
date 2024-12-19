package year_2024.day_019

case class Towel(stripes:List[Char])

case class TowelCombination(stripes:List[Char], lesserCombinations: List[LesserCombination])

case class LesserCombination(startStripe: List[Char], count:Int)

case class Design(stripes:List[Char])
