package react
package clipboard

import org.scalatest._
// import scala.scalajs.js
// import scala.scalajs.js.|
// import js.JSConverters._
import japgolly.scalajs.react.test._
import japgolly.scalajs.react.vdom.html_<^.{< => <<, _}
// import cats.syntax.eq._

class CopyToClipboardSpec extends FlatSpec with Matchers with NonImplicitAssertions with TestUtils {

  "CopyToClipboard" should
    "support rendering" in {
      val table = CopyToClipboard(CopyToClipboard.props(text = "text"), <<.div("Copy"))
      ReactTestUtils.withRenderedIntoDocument(table) { m =>
        val html =
          """<div>Copy</div>""".stripMargin.replaceAll("[\n\r]", "")
        assert(m.outerHtmlScrubbed() == html)
      }
    }
    it should "support text" in {
      CopyToClipboard(CopyToClipboard.props("text")).props.text should be("text")
    }
    it should "support onCopy callback" in {
      CopyToClipboard(CopyToClipboard.props("text")).props.onCopy should be(())
    }
}
