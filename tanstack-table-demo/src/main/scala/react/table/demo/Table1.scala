// Copyright (c) 2016-2022 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package react.table.demo

import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import lucuma.react.table.*
import react.common.*
import reactST.tanstackTableCore.mod.ColumnSort
import reactST.tanstackTableCore.mod.InitialTableState

import scalajs.js

object Table1:
  val component =
    ScalaFnComponent
      .withHooks[List[Guitar]]
      // cols
      .useMemo(())(_ =>
        List(
          ColumnDef[Guitar]("id", _.id, "Id1", ctx => s"g-${ctx.value}").sortAsc,
          // TODO Facade for Cell/Header, Context instead of extension for .value?
          ColumnDef[Guitar]("make", _.make, _ => "Make"),
          ColumnDef[Guitar]("model", _.model, _ => "Model").sortAscBy(_.length),
          ColumnDef.Group[Guitar](
            "details",
            _ => "Details",
            List(
              ColumnDef[Guitar]("year", _.details.year, _ => "Year"),
              ColumnDef[Guitar]("pickups", _.details.pickups, _ => "Pickups"),
              ColumnDef[Guitar]("color", _.details.color, _ => "Color", enableSorting = false)
            )
          )
        )
      )
      // rows
      .useMemoBy((guitars, _) => guitars)((_, _) => identity)
      // table
      .useReactTableBy { (_, cols, rows) =>
        TableOptions(
          cols,
          rows,
          enableSorting = true,
          initialState =
            InitialTableState().setSorting(js.Array(ColumnSort(desc = true, id = "model")))
        )
      }
      .render { (_, _, _, table) =>
        React.Fragment(
          <.h2("Sortable table"),
          HTMLTable(table, Css("guitars")),
          "Click header to sort. Shift-Click for multi-sort."
        )
      }
