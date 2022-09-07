// Copyright (c) 2016-2022 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package react.table.demo

import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import react.common.style.Css
import reactST.tanstackTableCore.mod.{
  AccessorFn => _,
  ColumnDef => _,
  TableOptions => RawOptions,
  ^ => _,
  *
}
import reactST.tanstackTableCore.anon.*
import lucuma.react.table.*
import react.common.*

import org.scalajs.dom
import scalajs.js
import scalajs.js.JSConverters.*

object Table1:
  private val SortedTable =
    ScalaFnComponent
      .withHooks[List[Guitar]]
      // cols
      .useMemo(())(_ =>
        List(
          ColumnDef[Guitar]("id", _.id, _ => "Id", ctx => s"g-${ctx.value}").sortAsc,
          // TODO Facade for Cell/Header, Context instead of extension?
          ColumnDef[Guitar]("make", _.make, _ => "Make", _.value),
          ColumnDef[Guitar]("model", _.model, _ => "Model", _.value).sortAscBy(_.length),
          ColumnDef.Group[Guitar](
            "details",
            _ => "Details",
            List(
              ColumnDef("year", _.details.year, _ => "Year", _.value),
              ColumnDef("pickups", _.details.pickups, _ => "Pickups", _.value),
              ColumnDef("color", _.details.color, _ => "Color", _.value, enableSorting = false)
            )
          )
        )
      )
      // rows
      .useMemoBy((guitars, _) => guitars)((_, _) => guitars => guitars)
      .customBy { (_, cols, rows) =>
        TableHook.useTableHook(
          TableOptions(
            cols,
            rows,
            enableSorting = true
          )
        )
      }
      // .useTableBy((_, cols, rows) =>
      //   SortedTableDef(cols, rows, Reusable.always(_.setInitialState(sortedTableState)))
      // )
      .render { (_, _, _, table) =>
        HTMLTable(table)
      }

  val component = ScalaFnComponent[List[Guitar]] { guitars =>
    React.Fragment(
      <.h2("Sortable table"),
      SortedTable(guitars),
      "Click header to sort. Shift-Click for multi-sort."
    )
  }
