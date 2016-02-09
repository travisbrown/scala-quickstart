sealed trait IntOption {
  def fold[Z](ifNone: Z, ifSome: Int => Z): Z = this match {
    case NoInt => ifNone
    case SomeInt(value) => ifSome(value)
  }
}

final case object NoInt extends IntOption
final case class SomeInt(value: Int) extends IntOption


sealed trait MyIntList {
  def toList: List[Int] = ???

  def fold[Z](ifEmpty: Z, ifCell: (Int, Z) => Z): Z = ???
}

final case object Empty extends MyIntList
final case class Cell(head: Int, tail: MyIntList) extends MyIntList
