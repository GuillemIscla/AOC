package year_2024.day_009

trait MemoryItem {
  def initial:BigInt
  def size:BigInt
  def checksum:BigInt
}

case class File(id:BigInt, initial:BigInt, size:BigInt) extends MemoryItem {
  override def checksum:BigInt = (initial until initial + size).map{
    i => i * id
  }.sum

}
case class FreeSpace(initial:BigInt, size:BigInt) extends MemoryItem {
  override def checksum:BigInt = 0
}

case class DiskMap(raw:List[MemoryItem]) {
  def moveBlock:DiskMap = {
    val freeSpaceIndex = raw.indexWhere {
      case _: FreeSpace => true
      case _ => false
    }
    val freeSpace = raw.collectFirst{
      case freeSpace: FreeSpace => freeSpace
    }.get

    val fileIndex = {
      val reverseIndex = raw.reverse.indexWhere {
        case _: File => true
        case _ => false
      }
      raw.size - reverseIndex
    }
    val file = raw.reverse.collectFirst {
      case file: File => file
    }.get

    val blockBefore = raw(freeSpaceIndex - 1)
    val newFreeSpace = Option.when(freeSpace.size > 1)(FreeSpace(freeSpace.initial + 1, freeSpace.size -1)).toList
    val newFile = Option.when(file.size > 1)(File(file.id, file.initial, file.size -1)).toList
    val afterNewFile = raw.drop(fileIndex - 1) match {
      case File(_, initial, size) :: Nil => FreeSpace(initial + size -1, 1) :: Nil
      case (_:File) :: FreeSpace(initial, size) :: Nil => List(FreeSpace(initial - 1, size + 1 ))
      case (_:File) :: FreeSpace(initialPrevious, sizePrevious) :: FreeSpace(_, size) :: Nil => List(FreeSpace(initialPrevious - 1, sizePrevious + size + 1 ))
      case _ => List.empty
    }
    DiskMap(mergeFile(raw.take(freeSpaceIndex), File(file.id, blockBefore.initial + blockBefore.size, 1)) ++
      newFreeSpace ++
      raw.slice(freeSpaceIndex + 1, freeSpaceIndex + 1 + fileIndex - freeSpaceIndex - 2) ++
      newFile ++
      afterNewFile
    )
  }
  def freeSpaceCount:BigInt = {
    val count = raw.count{
      case _: FreeSpace => true
      case _ => false
    }
    if(count == 2){
      raw.takeRight(2) match {
        case (_:FreeSpace) :: (_:FreeSpace) :: Nil => 1
        case _ => 2
      }
    }
    else {
      count
    }
  }

  def checksum:BigInt = raw.map(_.checksum).sum

  private def mergeFile(firsts:List[MemoryItem], file: File):List[MemoryItem] = {
    firsts match {
      case before :+ File(id, initial, size) if id == file.id => before :+ File(id,initial,size + file.size)
      case other => other :+ file
    }
  }
}
