// Copyright (c) 2016-2022 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package react.table.demo

import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import react.common.style.Css
// import reactST.reactTable.HTMLTable
// import reactST.reactTable.TableDef
// import reactST.reactTable.*
import lucuma.react.table.*
import react.common.*

object Table2:
  private def rowClassEvenOdd[D]: (Int, D) => Css = (i, _) =>
    if (i % 2 == 0) Css("even") else Css("odd")

  // private val VirtualizedTableDef = TableDef[Person].withBlockLayout

  val component =
    ScalaFnComponent
      .withHooks[List[Person]]
      // cols
      .useMemo(())(_ =>
        List(
          ColumnDef[Person]("first", _.first, _ => "First", _.value), // .setWidth(100),
          ColumnDef[Person]("last", _.last, _ => "Last", _.value),    // .setWidth(100),
          ColumnDef[Person]("age", _.age, _ => "Age", _.value)        // .setWidth(50),
        )
      )
      // rows
      .useMemoBy((people, _) => people)((_, _) => identity)
      .useReactTableBy((_, cols, rows) => TableOptions(cols, rows))
      .render((_, _, _, table) =>
        React.Fragment(
          <.h2("Virtualized Table"),
          HTMLTableVirtualized(table, containerClazz = Css("container"))
        )
        // HTMLTable.virtualized(VirtualizedTableDef)(
        //   tableClass = Css("virtualized"),
        //   rowClassFn = rowClassEvenOdd,
        //   headerCellFn = Some(HTMLTable.basicHeaderCellFn(useDiv = true))
        // )(tableInstance)
      )

  // val component = ScalaFnComponent[List[Person]] { people =>
  //   React.Fragment(
  //     <.h2("Virtualized Table"),
  //     VirtualizedTable(people)
  //   )
  // }
