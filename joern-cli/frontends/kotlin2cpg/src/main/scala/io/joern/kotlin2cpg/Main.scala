package io.joern.kotlin2cpg

import io.joern.kotlin2cpg.Frontend._
import io.joern.x2cpg.{X2CpgConfig, X2CpgMain}
import scopt.OParser

case class DefaultContentRootJarPath(path: String, isResource: Boolean)

final case class Config(
  inputPaths: Set[String] = Set.empty,
  outputPath: String = X2CpgConfig.defaultOutputPath,
  classpath: Set[String] = Set.empty,
  withStdlibJarsInClassPath: Boolean = true,
  withAndroidJarsInClassPath: Boolean = true,
  downloadDependencies: Boolean = false
) extends X2CpgConfig[Config] {

  override def withAdditionalInputPath(inputPath: String): Config =
    copy(inputPaths = inputPaths + inputPath)

  override def withOutputPath(x: String): Config = copy(outputPath = x)
}

private object Frontend {
  implicit val defaultConfig: Config = Config()

  val cmdLineParser: OParser[Unit, Config] = {
    val builder = OParser.builder[Config]
    import builder.programName
    import builder.opt
    OParser.sequence(
      programName("kotlin2cpg"),
      opt[String]("classpath")
        .unbounded()
        .text("Add entry to classpath")
        .action((incl, c) => c.copy(classpath = c.classpath + incl)),
      opt[Unit]("no-stdlib-jars")
        .text("Do not add local versions of Kotlin stdlib jars to classpath")
        .action((_, c) => c.copy(withStdlibJarsInClassPath = false)),
      opt[Unit]("no-android-jars")
        .text("Do not add local versions of Android jars to classpath")
        .action((_, c) => c.copy(withAndroidJarsInClassPath = false)),
      opt[Unit]("copy-runtime-libs") // deprecated, remove the flag as soon it's not needed upstream
        .text("Attempt to copy the runtime libs using the build tool found at the input path")
        .action((_, c) => c.copy(downloadDependencies = true)),
      opt[Unit]("download-dependencies")
        .text("Download the dependencies of the target project and add them to the classpath")
        .action((_, c) => c.copy(downloadDependencies = true))
    )
  }
}

object Main extends X2CpgMain(cmdLineParser, new Kotlin2Cpg()) {
  def run(config: Config, kotlin2cpg: Kotlin2Cpg): Unit = {
    kotlin2cpg.run(config)
  }
}
