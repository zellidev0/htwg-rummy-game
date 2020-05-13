import monocle.macros.GenLens
import org.scalatest.FunSuite

class Test extends FunSuite {

  test("PlayerModule can use MainModule sub-project") {
    val entity = Entity("id", NestedEntity("value"))
  }

  test("PlayerModule can use monocle dependency ") {
    val idLens = GenLens[Entity](_.id)
  }
}
