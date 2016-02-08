import org.scalatest.FlatSpec
import org.scalatest.prop.Checkers

class CalculatorSpec extends FlatSpec with Checkers {
  "A Calculator" should "add integers" in {
    check { (a: Int, b: Int) =>
      Calculator.+(Success(a), b) === Success(a + b)
    }
  }

  it should "subtract integers" in {
    check { (a: Int, b: Int) =>
      Calculator.-(Success(a), b) === Success(a - b)
    }
  }

  it should "divide integers safely" in {
    check { (a: Int, b: Int) =>
      val result = Calculator./(Success(a), b)

      if (b == 0) {
        result === Failure("Division by zero")
      } else {
        result === Success(a / b)
      }
    } 
  }
}
