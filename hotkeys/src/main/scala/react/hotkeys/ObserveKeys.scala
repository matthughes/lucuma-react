// Copyright (c) 2016-2022 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package react.hotkeys

import japgolly.scalajs.react._
import react.common._

import scalajs.js
import js.|
import js.annotation.JSImport

// This is not working if "only" or "except" are specified, resulting in a runtime exception.
// Haven't figured out the internals in order to pass properties another way.
final case class ObserveKeys(
  only:                  js.UndefOr[String | js.Array[String]] = js.undefined,
  except:                js.UndefOr[String | js.Array[String]] = js.undefined,
  override val children: CtorType.ChildrenArgs = Seq.empty
) extends GenericComponentPC[ObserveKeys.ObserveKeysProps, ObserveKeys] {
  override protected def cprops                     = ObserveKeys.props(this)
  def withChildren(children: CtorType.ChildrenArgs) =
    copy(children = children)
  val component                                     = ObserveKeys.component
}

object ObserveKeys {

  @js.native
  trait ObserveKeysProps extends js.Object {
    var only: js.UndefOr[String | js.Array[String]]   = js.native
    var except: js.UndefOr[String | js.Array[String]] = js.native
  }

  def props(
    q: ObserveKeys
  ): ObserveKeysProps = {
    val p = (new js.Object).asInstanceOf[ObserveKeysProps]
    q.only.foreach((v: String | js.Array[String]) => p.only = v)
    q.except.foreach((v: String | js.Array[String]) => p.except = v)
    p
  }

  @JSImport("react-hotkeys", "ObserveKeys")
  @js.native
  object RawComponent extends js.Object

  private val component =
    JsComponent[ObserveKeysProps, Children.Varargs, Null](RawComponent)

}
