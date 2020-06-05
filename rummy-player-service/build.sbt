name := "rummy-player-service"
version := "0.1"
scalaVersion := "2.12.7"

libraryDependencies +=  "com.typesafe.akka"      %% "akka-http"           % "10.1.12"
libraryDependencies +=  "com.typesafe.akka"      %% "akka-stream"         % "2.5.26"
libraryDependencies +=  "org.scalactic"          %% "scalactic"           % "3.0.5"
libraryDependencies +=  "org.scalatest"          %% "scalatest"           % "3.0.5" % "test"
libraryDependencies +=  "com.google.inject"      % "guice"                % "4.1.0"
libraryDependencies +=  "net.codingwell"         %% "scala-guice"         % "4.1.0"
libraryDependencies +=  "org.scala-lang.modules" % "scala-xml_2.12"       % "1.0.6"
libraryDependencies +=  "com.typesafe.play"      %% "play-json"           % "2.6.6"
libraryDependencies +=  "org.scala-lang.modules" % "scala-swing_2.12"     % "2.0.3"
libraryDependencies +=  "com.typesafe.akka"      %% "akka-stream-testkit" % "2.5.26"
libraryDependencies +=  "com.typesafe.akka"      %% "akka-http-testkit"   % "10.1.12"
libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.3.2",
  "org.slf4j" % "slf4j-nop" % "1.6.4"
)

unmanagedBase := baseDirectory.value / "lib"