package io.joern.javasrc2cpg.querying

import io.joern.javasrc2cpg.testfixtures.JavaSrcCodeToCpgFixture
import io.shiftleft.semanticcpg.language._
import org.scalatest.Ignore

class MethodReturnTests extends JavaSrcCodeToCpgFixture {

  override val code: String =
    """class Foo {
      |  int foo() { return 1; }
      |}
      |""".stripMargin

  "should have METHOD_RETURN node with correct fields" in {
    val List(x) = cpg.method.name("foo").methodReturn.typeFullName("int").l
    x.code shouldBe "int"
    x.typeFullName shouldBe "int"
    x.lineNumber shouldBe Some(2)
    // we expect the METHOD_RETURN node to be the right-most
    // child so that when traversing the AST from left to
    // right in CFG construction, we visit it last.
    x.order shouldBe 4
  }

  "should have a RETURN node ith correct fields" in {
    val List(x) = cpg.method.name("foo").ast.isReturn.l
    x.code shouldBe "return 1;"
    x.order shouldBe 1
    x.argumentIndex shouldBe 1
    x.astChildren.size shouldBe 1
    x.argumentOut.size shouldBe 1
  }

  "should allow traversing to method" in {
    cpg.methodReturn.code("int").method.name.l shouldBe List("foo")
  }

}
