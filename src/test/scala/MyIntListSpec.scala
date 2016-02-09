import org.scalacheck.{ Arbitrary, Gen }
import org.scalatest.FlatSpec
import org.scalatest.prop.Checkers

class MyIntListSpec extends FlatSpec with Checkers {
  implicit val arbitraryMyIntList: Arbitrary[MyIntList] = Arbitrary(
    Gen.oneOf(
      for {
        head <- Arbitrary.arbitrary[Int]
        tail <- Arbitrary.arbitrary[MyIntList]
      } yield Cell(head, tail),
      Gen.const(Empty)
    )
  )

  "MyIntList" should "be convertible to a regular list with fold" in {
    check { (l: MyIntList) =>
      l.fold[List[Int]](Nil, (head, tail) => head :: tail) === l.toList
    }
  }
}
