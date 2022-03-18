name := "php2cpg"

scalaVersion := "2.13.8"
crossScalaVersions := Seq("2.13.8", "3.1.1")

dependsOn(Projects.x2cpg)

libraryDependencies ++= Seq(
  "com.lihaoyi"   %% "ujson" % "1.5.0",
  "io.shiftleft"  %% "codepropertygraph"        % Versions.cpg,
  "io.shiftleft"  %% "semanticcpg"              % Versions.cpg,
  "org.scalatest" %% "scalatest"                % Versions.scalatest % Test
)

scalacOptions ++= Seq(
  "-deprecation" // Emit warning and location for usages of deprecated APIs.
)

enablePlugins(JavaAppPackaging)
Global / onChangedBuildSource := ReloadOnSourceChanges
