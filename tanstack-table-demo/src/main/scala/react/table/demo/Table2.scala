// Copyright (c) 2016-2022 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package react.table.demo

import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import react.common.style.Css
import lucuma.react.table.*
import react.common.*

object Table2:
  private def rowClassEvenOdd[T]: (Int, T) => Css = (i, _) =>
    if (i % 2 == 0) Css("even") else Css("odd")

  val component =
    ScalaFnComponent
      .withHooks[List[Person]]
      // cols
      .useMemo(())(_ =>
        List(
          ColumnDef[Person]("first", _.first, _ => "First"), // .setWidth(100),
          ColumnDef[Person]("last", _.last, _ => "Last"),    // .setWidth(100),
          ColumnDef[Person]("age", _.age, _ => "Age")        // .setWidth(50),
        )
      )
      // rows
      .useMemoBy((people, _) => people)((_, _) => identity)
      .useReactTableBy((_, cols, rows) => TableOptions(cols, rows, enableSorting = true))
      .render((_, _, _, table) =>
        React.Fragment(
          <.h2("Sortable Virtualized Table"),
          HTMLTableVirtualized(
            table,
            containerClass = Css("container"),
            rowClassFn = rowClassEvenOdd
          )
        )
      //   rowClassFn = rowClassEvenOdd,
      )
