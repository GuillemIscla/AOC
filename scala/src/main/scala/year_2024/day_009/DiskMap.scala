package year_2024.day_009

import scala.collection.immutable.Range.BigInt

trait MemoryItem {
  def initial:BigInt
  def size:BigInt
  def checksum:BigInt
  def prettyPrint:String
}

case class File(id:BigInt, initial:BigInt, size:BigInt) extends MemoryItem {
  override def checksum:BigInt = (initial until initial + size).map{
    i => i * id
  }.sum

  override def prettyPrint: String = {
    val printId:Int = if(id == 46) 200 else id.toInt + 33
    List.fill(size.toInt)(s"${printId.toChar}").mkString
    List.fill(size.toInt)(s"$id").mkString
  }
}
case class FreeSpace(initial:BigInt, size:BigInt) extends MemoryItem {
  override def checksum:BigInt = 0
  override def prettyPrint: String = {
    List.fill(size.toInt)(s".").mkString
  }
}

case class DiskMap(raw:List[MemoryItem]) {
  def moveFile(id:BigInt):DiskMap = {
    val fileToMove = raw.collectFirst {
      case file@File(fileId, _, _) if fileId == id => file
    }.get

    val firstFree = raw.collectFirst {
      case freeSpace@FreeSpace(_, size) if size >= fileToMove.size => freeSpace
    }

    firstFree match {
      case Some(freeSpace: FreeSpace) if freeSpace.initial < fileToMove.initial =>
        val freeSpaceIndex = raw.indexOf(freeSpace)
        val fileToMoveIndex = raw.indexOf(fileToMove)
        val newFreeSpace =
          if(freeSpace.size > fileToMove.size)
            List(File(fileToMove.id, freeSpace.initial, fileToMove.size), FreeSpace(freeSpace.initial + fileToMove.size, freeSpace.size - fileToMove.size))
          else  List(File(id, freeSpace.initial, fileToMove.size))

        DiskMap(
          raw.take(freeSpaceIndex) ++
            newFreeSpace ++
            raw.slice(freeSpaceIndex + 1, fileToMoveIndex) ++
            List(FreeSpace(fileToMove.initial, fileToMove.size)) ++
            raw.drop(fileToMoveIndex + 1)
        )

      case _ => this
    }
  }
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
    val newRaw = mergeFile(raw.take(freeSpaceIndex), File(file.id, blockBefore.initial + blockBefore.size, 1)) ++
      newFreeSpace ++
      raw.slice(freeSpaceIndex + 1, freeSpaceIndex + 1 + fileIndex - freeSpaceIndex - 2) ++
      newFile ++
      afterNewFile
    DiskMap(polishLasts(newRaw))
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

  def prettyPrint:String = raw.map(_.prettyPrint).mkString

  private def mergeFile(firsts:List[MemoryItem], file: File):List[MemoryItem] = {
    firsts match {
      case before :+ File(id, initial, size) if id == file.id => before :+ File(id,initial,size + file.size)
      case other => other :+ file
    }
  }

  private def polishLasts(newRaw:List[MemoryItem]):List[MemoryItem] = {
    newRaw.dropRight(4) ++ newRaw.takeRight(4).foldRight(List.empty[MemoryItem]){
      case (FreeSpace(_, size2), FreeSpace(position1, size1) :: Nil) => List(FreeSpace(position1, size1 + size2))
      case (File(id1, position1, size1), File(id2, _, size2) :: tail) if id1 == id2 =>
        File(id1, position1, size1 + size2) :: tail
      case (memItem, list) => memItem :: list
    }
  }
}
