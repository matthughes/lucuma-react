// Copyright (c) 2016-2022 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package lucuma.react.table

import japgolly.scalajs.react.*
import japgolly.scalajs.react.hooks.CustomHook
import reactST.tanstackReactTable.mod.useReactTable
import reactST.{tanstackTableCore => raw}
import lucuma.react.table.facade.*

import scala.scalajs.js
import scala.scalajs.js.JSConverters.*
import scala.scalajs.js.annotation.JSImport

object TableHook {
  private def useTableJS[T] =
    CustomHook.unchecked[TableOptionsJS[T], raw.mod.Table[T]](opts =>
      useReactTable(opts.asInstanceOf[raw.mod.TableOptions[T]])
    )

  def useTableHook[T] =
    CustomHook[TableOptions[T]]
      .useMemoBy(_.columns)(_ => _.toJSArray.map(_.toJS))
      .useMemoBy(_.input.data)(_ => _.toJSArray)
      .customBy { (props, cols, rows) =>
        val options = TableOptionsJS(
          columns = cols,
          data = rows,
          getCoreRowModel = props.getCoreRowModel.map(fn => fn), // Forces conversion to js.Function
          onStateChange = props.onStateChange,
          // ...
          enableSorting = props.enableSorting,
          getSortedRowModel = props.getSortedRowModel.map(fn => fn)
        )

        // org.scalajs.dom.console.log("OPTIONS", options)

        val table =
          useTableJS[T](options)

        table
      }
      .buildReturning((_, _, _, table) => table)
}
