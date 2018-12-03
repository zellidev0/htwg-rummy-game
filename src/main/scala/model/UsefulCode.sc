
import scala.util.matching.Regex


//val LayDownTilePattern: Regex = new Regex("l [1-9]|1[0123][RBYG][01]")
val LayDownTilePattern: Regex = "l [1-9][RBGY][01]|l 1[0123][RBGY][01]".r

var input = "l 1G0"
input match {
  case LayDownTilePattern() => println("Yes")
  case _ => print("FALSE")
}
