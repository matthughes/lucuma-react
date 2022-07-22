// Copyright (c) 2016-2022 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package react.datepicker

import japgolly.scalajs.react.Callback
import japgolly.scalajs.react.ReactEventFrom
import lucuma.reactDatepicker.mod.ReactDatePickerProps
import lucuma.reactDatepicker.mod.default
import org.scalajs.dom.Element
import org.scalablytyped.runtime.StObject
import scala.scalajs.js
import scala.scalajs.js.`|`
import scala.scalajs.js.annotation.{JSBracketAccess, JSGlobal, JSGlobalScope, JSImport, JSName}
import lucuma.reactDatepicker.components.ReactDatepicker.Builder

// Copy+pasted from the facade generated by ST.
// For the moment, we cat't use the facade generated by ST because react-datepicker defines 2
// types that just differ in casing (ReactDatePicker vs ReactDatepicker). The one we should
// call is ReactDatepicker, which has a default JSImport, but ST chooses ReactDatePicker,
// which has a named import that causes a runtime error.
object Datepicker {

  @scala.inline
  def apply[CustomModifierNames /* <: String */, WithRange](
    onChange: (
      js.UndefOr[
        js.Date | /* for selectsRange */ Null | (js.Tuple2[js.Date | Null, js.Date | Null])
      ],
      js.UndefOr[ReactEventFrom[js.Any with Element]]
    ) => Callback
  ): Builder[CustomModifierNames, WithRange] = {
    val __props = js.Dynamic.literal(onChange =
      js.Any.fromFunction2(
        (
          t0: js.UndefOr[
            js.Date | /* for selectsRange */ Null | (js.Tuple2[js.Date | Null, js.Date | Null])
          ],
          t1: js.UndefOr[ReactEventFrom[js.Any with Element]]
        ) => onChange(t0, t1).runNow()
      )
    )
    new Builder[CustomModifierNames, WithRange](
      js.Array(this.component,
               __props.asInstanceOf[ReactDatePickerProps[CustomModifierNames, WithRange]]
      )
    )
  }

  @JSImport("react-datepicker", JSImport.Default)
  @js.native
  val component: js.Object = js.native

  def withProps[CustomModifierNames /* <: String */, WithRange](
    p: ReactDatePickerProps[CustomModifierNames, ?]
  ): Builder[CustomModifierNames, WithRange] =
    new Builder[CustomModifierNames, WithRange](js.Array(this.component, p.asInstanceOf[js.Any]))
}
