// Copyright (c) 2016-2022 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package react.semanticui.modules.popup

import japgolly.scalajs.react.test._
import japgolly.scalajs.react.vdom.html_<^._
import react.common.syntax.vdom._

class PopupSuite extends munit.FunSuite {
  test("render") {
    val popup = Popup("Abc")
    ReactTestUtils.withNewBodyElement { mountNode =>
      popup.renderIntoDOM(mountNode)
      assertEquals(mountNode.outerHTML, """<div></div>""")
    }
  }
}
