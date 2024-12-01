package year_2023.day_025


case class Component(name:String){
  var wired:List[Component] = List.empty
  def addComponent(component: Component):Unit =
    wired = component :: wired
}
case class Apparatus(components:List[Component])
