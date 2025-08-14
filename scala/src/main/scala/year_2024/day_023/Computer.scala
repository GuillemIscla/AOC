package year_2024.day_023

case class Computer(name:String){
  def startsWithT:Boolean = name.head == 't'

  override def toString: String = name
}

case class ComputerLink(a:String, b:String){
  def isLink(c:Computer, d:Computer):Boolean =
    (c.name == a && d.name == b) ||
      (d.name == a && c.name == b)

  def has(c:Computer):Boolean = a == c.name || b == c.name
}