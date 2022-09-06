// Copyright (c) 2016-2022 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package lucuma.react.table

import japgolly.scalajs.react.vdom.VdomNode
import reactST.{tanstackTableCore => raw}
import lucuma.react.table.facade.*

import scalajs.js
import scalajs.js.JSConverters.*

sealed trait ColumnDef[T, A]:
  val id: String
  val header: js.UndefOr[raw.mod.HeaderContext[T, A] => VdomNode]
  val footer: js.UndefOr[raw.mod.HeaderContext[T, A] => VdomNode]
  val meta: js.UndefOr[Any]

  // Sorting
  val enableMultiSort: js.UndefOr[Boolean]              = js.undefined
  val enableSorting: js.UndefOr[Boolean]                = js.undefined
  val invertSorting: js.UndefOr[Boolean]                = js.undefined
  val sortDescFirst: js.UndefOr[Boolean]                = js.undefined
  // var sortUndefined: js.UndefOr[`false` | `-1` | `1`] = js.undefined
  val sortingFn: js.UndefOr[raw.mod.SortingFnOption[A]] = js.undefined

  def toJS: ColumnDefJS[T, A]

object ColumnDef:
  case class Single[T, A](
    id:       String,
    accessor: T => A,
    header:   js.UndefOr[raw.mod.HeaderContext[T, A] => VdomNode] = js.undefined,
    cell:     raw.mod.CellContext[T, A] => VdomNode,
    footer:   js.UndefOr[raw.mod.HeaderContext[T, A] => VdomNode] = js.undefined,
    meta:     js.UndefOr[Any] = js.undefined
  ) extends ColumnDef[T, A]:
    def toJS: ColumnDefJS[T, A] = {
      val p: ColumnDefJS[T, A] = new js.Object().asInstanceOf[ColumnDefJS[T, A]]
      p.id = id
      p.accessorFn = accessor
      p.header = header.map(fn => fn.andThen(_.rawNode))
      p.cell = cell.andThen(_.rawNode)
      p.footer = footer.map(fn => fn.andThen(_.rawNode))
      p.meta = meta
      p
    }

  case class Group[T](
    id:      String,
    header:  js.UndefOr[raw.mod.HeaderContext[T, Nothing] => VdomNode] = js.undefined,
    columns: List[ColumnDef[T, ?]],
    footer:  js.UndefOr[raw.mod.HeaderContext[T, Nothing] => VdomNode] = js.undefined,
    meta:    js.UndefOr[Any] = js.undefined
  ) extends ColumnDef[T, Nothing]:
    def toJS: ColumnDefJS[T, Nothing] = {
      val p: ColumnDefJS[T, Nothing] = new js.Object().asInstanceOf[ColumnDefJS[T, Nothing]]
      p.id = id
      p.header = header.map(fn => fn.andThen(_.rawNode))
      p.columns = columns.map(_.toJS).toJSArray
      p.footer = footer.map(fn => fn.andThen(_.rawNode))
      p.meta = meta
      p
    }

  def apply[T]: Applied[T] =
    new Applied[T]

  class Applied[T]:
    def apply[A](
      id:       String,
      accessor: T => A,
      header:   js.UndefOr[raw.mod.HeaderContext[T, A] => VdomNode] = js.undefined,
      cell:     raw.mod.CellContext[T, A] => VdomNode,
      footer:   js.UndefOr[raw.mod.HeaderContext[T, A] => VdomNode] = js.undefined,
      meta:     js.UndefOr[Any] = js.undefined
    ): Single[T, A] =
      Single(id, accessor, header, cell, footer, meta)
