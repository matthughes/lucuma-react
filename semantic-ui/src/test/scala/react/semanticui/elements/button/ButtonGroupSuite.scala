// Copyright (c) 2016-2022 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package react.semanticui.elements.button

import japgolly.scalajs.react.test._
import japgolly.scalajs.react.vdom.html_<^._
import react.common.GenericComponentPACOps
import react.common.syntax.vdom._
import react.semanticui.widths.widthOf

class ButtonGroupSuite extends munit.FunSuite {
  test("render") {
    val buttonGroup = ButtonGroup("abc")
    ReactTestUtils.withNewBodyElement { mountNode =>
      buttonGroup.renderIntoDOM(mountNode)
      assertEquals(mountNode.innerHTML, """<div class="ui buttons">abc</div>""")
    }
  }
  test("widths") {
    val buttonGroup =
      ButtonGroup(widths = widthOf(2)).apply(Button("1"), Button("2"))
    ReactTestUtils.withNewBodyElement { mountNode =>
      buttonGroup.renderIntoDOM(mountNode)
      assertEquals(
        mountNode.innerHTML,
        """<div class="ui two buttons"><button class="ui button">1</button><button class="ui button">2</button></div>"""
      )
    }
  }
  test("buttons") {
    val buttonGroup =
      new ButtonGroup().apply(Button("1"), Button("2"))
    ReactTestUtils.withNewBodyElement { mountNode =>
      buttonGroup.renderIntoDOM(mountNode)
      assertEquals(
        mountNode.innerHTML,
        """<div class="ui buttons"><button class="ui button">1</button><button class="ui button">2</button></div>"""
      )
    }
  }
}
