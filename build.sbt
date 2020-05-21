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
    MainModule,
    GameModule,
    PlayerModule,
    ControllerModule
  )

lazy val GameModule = project
  .settings(name := "GameModule", settings, assemblySettings, libraryDependencies ++= mainModuleDependencies )

lazy val PlayerModule = project
  .settings(name := "PlayerModule", settings, assemblySettings, libraryDependencies ++= mainModuleDependencies)

lazy val ControllerModule = project
  .settings(name := "ControllerModule", settings, libraryDependencies ++= mainModuleDependencies)
  .dependsOn(PlayerModule, GameModule)
  .aggregate(PlayerModule, GameModule)

lazy val MainModule = project
  .settings(name := "MainModule", settings, libraryDependencies ++= mainModuleDependencies, unmanagedBase := baseDirectory.value / "lib")
  .disablePlugins(AssemblyPlugin)
  .dependsOn(ControllerModule)
  .aggregate(ControllerModule)



lazy val mainModuleDependencies = Seq(
  "com.typesafe.akka" %% "akka-stream" % "2.5.6",
  "com.typesafe.akka" %% "akka-http"   % "10.1.12",
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
