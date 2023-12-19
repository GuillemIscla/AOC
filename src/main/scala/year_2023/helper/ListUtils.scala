package year_2023.helper

object ListUtils {

  implicit class ListSyntax[A](list:List[A]){
    def replace(i:Int, a:A):List[A] = list.take(i) ++ (a :: list.drop(i + 1))

  }

  implicit class TableSyntax[A](table: List[List[A]]) {
    def replaceTable(x: Int, y:Int, a: A): List[List[A]] = ListSyntax(table).replace(x, ListSyntax(table(x)).replace(y, a))

  }

}
