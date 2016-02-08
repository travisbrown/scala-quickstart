# Scala quickstart

This is a basic [SBT][sbt] project designed to help you start working
effectively in a Scala REPL (read-eval-print loop) as quickly as possible. Once
you've cloned the project, you can open the SBT console with the following
command:

```bash
./sbt
```

Note that you'll need to have [Java][java] installed on your machine for this to
work. Once you're in the SBT console you can type `console` to open a Scala
REPL. You should see something like this:

```scala
> console
[info] Starting scala interpreter...
[info] 
Welcome to Scala version 2.11.7 (Java HotSpot(TM) 64-Bit Server VM, Java 1.8.0_66).
Type in expressions to have them evaluated.
Type :help for more information.

scala> 
```

You can now type Scala definitions and expressions and they will be evaluated
immediately.

## REPL tips

Most of the time the REPL should just do what you'd expect it to do (try to
evaluate code and print the result), but in some cases you may need or want to
use special REPL-specific commands that it provides to accomplish certain
things.

### Defining companions

If you simply type the following definitions into the REPL as two separate
lines, you'll get an error, since the case class and the object won't be treated
as companions:

```scala
case class Foo(i: Int)
object Foo { def empty: Foo = Foo(0) }
```

In these situations you can use `:paste` to combine definitions into a single
compilation unit:

```scala
scala> :paste
// Entering paste mode (ctrl-D to finish)

case class Foo(i: Int)
object Foo { def empty: Foo = Foo(0) }

// Exiting paste mode, now interpreting.

defined class Foo
defined object Foo
```

### Copying and pasting entire session entries

Suppose you've got the following in a REPL session:

```scala
scala> val doubled = "a" * 2
doubled: String = aa

scala> doubled.size
res1: Int = 2
```

If you want to evaluate these definitions again (either in the same session or a
new one), you can copy and paste the entire block of text, including the
`scala>` prompts and the result output, and the REPL will recognize that that's
what's going on:

```scala
scala> scala> val doubled = "a" * 2

// Detected repl transcript paste: ctrl-D to finish.

doubled: String = aa

scala> doubled.size
res1: Int = 2

// Replaying 2 commands from transcript.
```

Finding the type of an expression:

You can use the `:type` command to make the REPL tell you the type of any
expression, even if that expression doesn't evaluate to a value:

```scala
scala> :type 1 / 0
Int
```

### Saving a REPL session to a file

You can use `:save` to write a REPL session to a file in the current directory:

```scala
scala> val doubled = "a" * 2
doubled: String = aa

scala> doubled.size
res0: Int = 2

scala> :save doubling.sc
```

`doubling.sc` will now contain the following:

```scala
val doubled = "a" * 2
doubled.size
```

Note that these definitions won't necessarily be a valid `.scala` file. I'm
using the `.sc` extension here so that SBT won't try to parse the file as Scala,
but you can use any other extension you wish.

### Loading Scala definitions into the REPL from a file

You can also use `:load` to import definitions from a file into the REPL:

```scala
scala> :load doubling.sc
Loading doubling.sc...
doubled: String = aa
res0: Int = 2
```

### Desugaring extension methods and other syntactic magic

Thanks to the magic of extension methods and implicit conversions, it's often
very difficult to determine why you can call certain methods on certain
expressions and where these methods are coming from. We've been writing the
following, for example:

```scala
scala> val doubled = "a" * 2
doubled: String = aa

scala> doubled.size
res0: Int = 2
```

But Scala's `String` is just an alias for `java.lang.String`, which doesn't have
`*` or `size` methods. If we want to know where these are coming from, we can
use Scala's reflection API in the REPL to find out:

```scala
scala> import scala.reflect.runtime.universe.{ reify, showCode }
import scala.reflect.runtime.universe.{reify, showCode}

scala> showCode(reify("a" * 2).tree)
res0: String = Predef.augmentString("a").*(2)
```

Now we can look up the `augmentString` method on `Predef` in the standard
library's API docs, which will lead us to [`StringOps`][string-ops].

This approach also works for syntactic sugar like `for`-comprehensions. For
example, it might not be clear why we can mix and match sequences and options in
a `for`-comprehension:

```scala
scala> showCode(reify(for { i <- 1 to 3; j <- Option(4) } yield i + j).tree)
```

This will print the following (which I've reformatted for clarity):

```scala
Predef.intWrapper(1).to(3).flatMap(
  ((i) => Option.option2Iterable(Option.apply(4).map(((j) => i.+(j)))))
)(IndexedSeq.canBuildFrom)
```

This takes some processing to understand, but at least everything is explicit.

## Scaladoc tips

The Scala standard library has [extensive API documentation][scala-scaladocs],
but some useful features of the Scaladoc interface aren't immediately obvious.

### Browsing symbolic operators

For example, suppose you see the symbol `/:` in some Scala code. You
could try to figure out what type it's being called on and search for that type,
but you can also browse symbolic operators like this directly in the Scaladoc
interface. To do this, click the small `#` in the upper left corner of the
screen.

![Operator list link](/screenshots/operators-01.png)

Now you can search the page for `/:`:

![Operator list](/screenshots/operators-02.png)

### Instance and companion object definitions

Another simple but useful feature (which I personally only discovered after
writing Scala for an embarrassingly long time) is the ability to switch easily
between the docs for a class or trait and its companion object. If you're
reading the docs for the `List` class, for example:

![Operator list link](/screenshots/class.png)

The circe with a "C" in it is a link that will take you to the docs for the
`List` companion object:

![Operator list link](/screenshots/object.png)

There's no need to search for `List` again to be able to select the object
instead of the class. Clicking the "O" will take you back to the class.

[java]: https://www.java.com/en/download/
[sbt]: http://www.scala-sbt.org
[scala-scaladocs]: http://www.scala-lang.org/api/2.11.7/#package
[string-ops]: http://www.scala-lang.org/api/2.11.7/#scala.collection.immutable.StringOps
