import mill._
import mill.scalalib.scalafmt.ScalafmtModule
import scalalib._
import $ivy.`net.java.dev.javacc:javacc:7.0.4`
import mill.define.Target
import org.javacc.parser.Main

object pythonParser extends SbtModule with ScalafmtModule {
  def scalaVersion = "2.13.1"

  // We only have one module in this build. Thus we dont need
  // the usual directory level introduced by the module name.
  // So our source will be in src/main/scala and not
  // py2cpg/src/main/scala
  override def millSourcePath = millOuterCtx.millSourcePath

  /*
  override def ivyDeps = Agg(
  )
   */

  def javaCCSourceFile = T.source {
    PathRef(os.pwd/"pythonGrammar.jj")
  }

  def javaCCGenerate = T {
    Main.mainProgram(Array(s"-OUTPUT_DIRECTORY=${T.dest}/io/shiftleft/pythonparser", javaCCSourceFile().path.toString))
    os.walk(T.dest).filter(path => os.isFile(path) && path.ext == "java").map(PathRef(_))
  }

  override def generatedSources = T {
    super.generatedSources() ++ javaCCGenerate()
  }

  object test extends Tests with ScalafmtModule {
    override def ivyDeps = Agg(ivy"org.scalatest::scalatest:3.2.2")
    def testFrameworks = Seq("org.scalatest.tools.Framework")
  }
}

