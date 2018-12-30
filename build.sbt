name := "rummy"
version := "0.1"
organization := "de.htwg.se"
scalaVersion := "2.12.7"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

libraryDependencies += "org.scala-lang.modules" % "scala-xml_2.12" % "1.0.6"

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.6"

libraryDependencies += "org.scala-lang.modules" % "scala-swing_2.12" % "2.0.3"


// Add dependency on ScalaFX library
libraryDependencies += "org.scalafx" %% "scalafx" % "11-R16"




//// Determine OS version of JavaFX binaries
//lazy val osName = System.getProperty("os.name") match {
//  case n if n.startsWith("Linux")   => "linux"
//  case n if n.startsWith("Mac")     => "mac"
//  case n if n.startsWith("Windows") => "win"
//  case _ => throw new Exception("Unknown platform!")
//}

lazy val javaFXModules = Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
libraryDependencies ++= javaFXModules.map(m =>
  "org.openjfx" % s"javafx-$m" % "11" classifier "linux"
)