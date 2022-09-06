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
          ColumnDef[Guitar]("id", _.id, ctx => s"g-${ctx.getValue()}", _ => "Id"),
          // TODO Facade for Cell/Header, Context
          ColumnDef[Guitar]("make", _.make, _.getValue().toString, _ => "Make"),
          ColumnDef[Guitar]("model", _.model, _.getValue().toString, _ => "Model"),
          ColumnDef.Group[Guitar](
            "details",
            List(
              ColumnDef("year", _.details.year, header = _ => "Year", cell = _.getValue().toString),
              ColumnDef("pickups", _.details.pickups, _.getValue().toString, _ => "Pickups"),
              ColumnDef("color", _.details.color, _.getValue().toString, _ => "Color")
            ),
            header = _ => "Details"
          )
          // SortedTableDef
          //   .Column("id", _.id)
          //   .setCell(cellProps => <.span(s"g-${cellProps.value}"))
          //   .setSortByAuto
          //   .setHeader("Id"),
          // SortedTableDef
          //   .Column("make", _.make)
          //   .setHeader("Make")
          //   .setCell(cell => <.span(s"${cell.value}"))
          //   .setSortByFn(_.toString),
          // SortedTableDef.Column("model", _.model).setHeader("Model"),
          // SortedTableDef
          //   .ColumnGroup(
          //     SortedTableDef.Column("year", _.details.year).setHeader("Year"),
          //     SortedTableDef.Column("pickups", _.details.pickups).setHeader("Pickups"),
          //     SortedTableDef
          //       .Column("color", _.details.color)
          //       .setHeader("Color")
          //       .setDisableSortBy(true)
          //   )
          //   .setHeader("Details")
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
