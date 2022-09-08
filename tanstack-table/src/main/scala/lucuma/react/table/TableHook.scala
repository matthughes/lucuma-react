// Copyright (c) 2016-2022 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package lucuma.react.table

import japgolly.scalajs.react.*
import japgolly.scalajs.react.hooks.CustomHook
import lucuma.react.table.facade.*
import reactST.tanstackReactTable.mod.useReactTable
import reactST.tanstackTableCore.tanstackTableCoreStrings.renderFallbackValue
import reactST.{tanstackTableCore => raw}

import scala.scalajs.js
import scala.scalajs.js.JSConverters.*
import scala.scalajs.js.annotation.JSImport

object TableHook:
  @JSImport("@tanstack/react-table", "useReactTable")
  @js.native
  private def useReactTableJS[T](options: TableOptionsJS[T]): raw.mod.Table[T] = js.native

  def useTableHook[T] =
    CustomHook[TableOptions[T]]
      .useMemoBy(_.columns)(_ => _.toJSArray.map(_.toJS))
      .useMemoBy(_.input.data)(_ => _.toJSArray)
      .buildReturning { (props, cols, rows) =>
        val options = TableOptionsJS(
          columns = cols,
          data = rows,
          getCoreRowModel = props.getCoreRowModel.map(fn => fn), // Forces conversion to js.Function
          onStateChange = props.onStateChange,
          renderFallbackValue = props.renderFallbackValue,
          state = props.state,
          initialState = props.initialState,
          // Sorting
          enableSorting = props.enableSorting,
          getSortedRowModel = props.getSortedRowModel.map(fn => fn),
          // Expanding
          enableExpanding = props.enableExpanding,
          getExpandedRowModel = props.getExpandedRowModel.map(fn => fn),
          getSubRows = props.getSubRows.map(fn => fn.andThen(_.toJSArray))
        )

        useReactTableJS[T](options)
      }
