package year_2024.day_005

case class Update(pages:List[Int]){
  def middle:Int =
    pages.drop((pages.size -1) / 2).head

}
