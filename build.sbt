import sbt.Keys.unmanagedBase

name := "rummy"
organization in ThisBuild := "de.htwg.se.rummy"
scalaVersion in ThisBuild := "2.12.7"

// PROJECTS

lazy val global = project
  .in(file("."))
  .settings(settings)
  .disablePlugins(AssemblyPlugin)
  .aggregate(
    mainModule,
    gameModule,
    playerModule,
    controllerModule
  )

lazy val gameModule = project
  .settings(name := "GameModule", settings, assemblySettings, libraryDependencies ++= mainModuleDependencies ++ Seq(
    dependencies.monocleCore, dependencies.monocleMacro))
  .dependsOn(controllerModule)

lazy val playerModule = project
  .settings(name := "PlayerModule", settings, assemblySettings, libraryDependencies ++= mainModuleDependencies ++ Seq(
    dependencies.pureconfig))
  .dependsOn(controllerModule)

lazy val controllerModule = project
  .settings(name := "ControllerModule", settings, libraryDependencies ++= mainModuleDependencies)
  .dependsOn(controllerModule)

lazy val mainModule = project
  .settings(name := "MainModule", settings, libraryDependencies ++= mainModuleDependencies, unmanagedBase := baseDirectory.value / "lib")
  .disablePlugins(AssemblyPlugin)
  .dependsOn(controllerModule)


// DEPENDENCIES
lazy val dependencies =
  new {
    val logbackV = "1.2.3"
    val logstashV = "4.11"
    val scalaLoggingV = "3.7.2"
    val slf4jV = "1.7.25"
    val typesafeConfigV = "1.3.1"
    val pureconfigV = "0.8.0"
    val monocleV = "1.4.0"
    val akkaV = "2.5.6"
    val scalatestV = "3.0.4"
    val scalacheckV = "1.13.5"

    val logback = "ch.qos.logback" % "logback-classic" % logbackV
    val logstash = "net.logstash.logback" % "logstash-logback-encoder" % logstashV
    val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingV
    val slf4j = "org.slf4j" % "jcl-over-slf4j" % slf4jV
    val typesafeConfig = "com.typesafe" % "config" % typesafeConfigV
    val akka = "com.typesafe.akka" %% "akka-stream" % akkaV
    val monocleCore = "com.github.julien-truffaut" %% "monocle-core" % monocleV
    val monocleMacro = "com.github.julien-truffaut" %% "monocle-macro" % monocleV
    val pureconfig = "com.github.pureconfig" %% "pureconfig" % pureconfigV
    val scalatest = "org.scalatest" %% "scalatest" % scalatestV
    val scalacheck = "org.scalacheck" %% "scalacheck" % scalacheckV
    val gguice = "com.google.inject" % "guice" % "4.1.0"
  }

lazy val mainModuleDependencies = Seq(
  dependencies.logback,
  dependencies.logstash,
  dependencies.scalaLogging,
  dependencies.slf4j,
  dependencies.typesafeConfig,
  dependencies.akka,
  dependencies.gguice,
  dependencies.scalatest % "test",
  dependencies.scalacheck % "test",
  "org.scalactic" %% "scalactic" % "3.0.5",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",
  "com.google.inject" % "guice" % "4.1.0",
  "net.codingwell" %% "scala-guice" % "4.1.0",
  "org.scala-lang.modules" % "scala-xml_2.12" % "1.0.6",
  "com.typesafe.play" %% "play-json" % "2.6.6",
  "org.scala-lang.modules" % "scala-swing_2.12" % "2.0.3",
  "org.scalafx" %% "scalafx" % "11-R16",
)

// SETTINGS

lazy val settings =
  commonSettings ++
    wartremoverSettings ++
    scalafmtSettings

lazy val compilerOptions = Seq(
  "-unchecked",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-deprecation",
  "-encoding",
  "utf8"
)

lazy val commonSettings = Seq(
  scalacOptions ++= compilerOptions,
  resolvers ++= Seq(
    "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository",
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots")
  )
)

lazy val wartremoverSettings = Seq(
  wartremoverWarnings in(Compile, compile) ++= Warts.allBut(Wart.Throw)
)

lazy val scalafmtSettings =
  Seq(
    scalafmtOnCompile := true,
    scalafmtTestOnCompile := true,
    scalafmtVersion := "1.2.0"
  )

lazy val assemblySettings = Seq(
  assemblyJarName in assembly := name.value + ".jar",
  assemblyMergeStrategy in assembly := {
    case PathList("META-INF", xs@_*) => MergeStrategy.discard
    case "application.conf" => MergeStrategy.concat
    case x =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  }
)
//name := "rummy"
//version := "0.1"
//organization := "de.htwg.se"
//scalaVersion := "2.12.7"
//
//libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"
//libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
//libraryDependencies += "com.google.inject" % "guice" % "4.1.0"
//libraryDependencies += "net.codingwell" %% "scala-guice" % "4.1.0"
//libraryDependencies += "org.scala-lang.modules" % "scala-xml_2.12" % "1.0.6"
//libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.6"
//libraryDependencies += "org.scala-lang.modules" % "scala-swing_2.12" % "2.0.3"
//libraryDependencies += "org.scalafx" %% "scalafx" % "11-R16"
//lazy val javaFXModules = Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
//libraryDependencies ++= javaFXModules.map(m =>
//  "org.openjfx" % s"javafx-$m" % "11" classifier "linux"
//)
//