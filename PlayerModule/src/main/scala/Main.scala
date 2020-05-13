import monocle.macros.GenLens

object Main extends App {
  println("PlayerModule can use MainModule sub-project")

  val entity = Entity("id", NestedEntity("value"))

  println("PlayerModule can use monocle dependency")

  val idLens = GenLens[Entity](_.id)
}
