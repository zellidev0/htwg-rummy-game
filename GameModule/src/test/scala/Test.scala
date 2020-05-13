import org.scalatest.FunSuite

class Test extends FunSuite {

  test("GameModule can use MainModule sub-project") {
    val entity = Entity("id", NestedEntity("value"))
  }

  test("GameModule can use pureconfig dependency") {
    import pureconfig._

    implicit def hint[T]: ProductHint[T] = ProductHint[T](ConfigFieldMapping(CamelCase, KebabCase))
  }
}
