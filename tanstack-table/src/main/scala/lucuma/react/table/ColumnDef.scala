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

  def toJS: ColumnDefJS[T, A]

object ColumnDef:
  case class Single[T, A](
    id:              String,
    accessor:        T => A,
    header:          js.UndefOr[raw.mod.HeaderContext[T, A] => VdomNode] = js.undefined,
    cell:            raw.mod.CellContext[T, A] => VdomNode,
    footer:          js.UndefOr[raw.mod.HeaderContext[T, A] => VdomNode] = js.undefined,
    meta:            js.UndefOr[Any] = js.undefined,
    // Sorting
    enableMultiSort: js.UndefOr[Boolean] = js.undefined,
    enableSorting:   js.UndefOr[Boolean] = js.undefined,
    invertSorting:   js.UndefOr[Boolean] = js.undefined,
    sortDescFirst:   js.UndefOr[Boolean] = js.undefined,
    // sortUndefined:   js.UndefOr[false | -1 | 1] = js.undefined,
    sortingFn:       js.UndefOr[raw.mod.SortingFn[A]] = js.undefined
  ) extends ColumnDef[T, A]:
    def sortBy[B](f: A => B, inverted: Boolean)(using ordering: Ordering[B]): ColumnDef[T, A] =
      val sbfn: raw.mod.SortingFn[A] = (r1, r2, col) =>
        ordering
          .compare(f(r1.getValue[A](col)), f(r2.getValue[A](col)))
          .toDouble
      copy(sortingFn = sbfn, enableSorting = true, invertSorting = inverted)

    def sortAscBy[B](f: A => B)(using ordering: Ordering[B]): ColumnDef[T, A] = sortBy(f, false)

    def sortDescBy[B](f: A => B)(using ordering: Ordering[B]): ColumnDef[T, A] = sortBy(f, true)

    def sortAsc(using Ordering[A]) = sortAscBy(identity)

    def sortDesc(using Ordering[A]) = sortDescBy(identity)

    def toJS: ColumnDefJS[T, A] = {
      val p: ColumnDefJS[T, A] = new js.Object().asInstanceOf[ColumnDefJS[T, A]]
      p.id = id
      p.accessorFn = accessor
      header.foreach(fn => p.header = fn.andThen(_.rawNode))
      p.cell = cell.andThen(_.rawNode)
      footer.foreach(fn => p.footer = fn.andThen(_.rawNode))
      meta.foreach(v => p.meta = v)
      // Sorting
      enableMultiSort.foreach(v => p.enableMultiSort = v)
      enableSorting.foreach(v => p.enableSorting = v)
      invertSorting.foreach(v => p.invertSorting = v)
      sortDescFirst.foreach(v => p.sortDescFirst = v)
      // sortUndefined.foreach(v => p.sortUndefined = v)
      sortingFn.foreach(fn => p.sortingFn = fn)
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
      header.foreach(fn => p.header = fn.andThen(_.rawNode))
      p.columns = columns.map(_.toJS).toJSArray
      footer.foreach(fn => p.footer = fn.andThen(_.rawNode))
      meta.foreach(v => p.meta = v)
      p
    }

  def apply[T]: Applied[T] =
    new Applied[T]

  class Applied[T]:
    def apply[A](
      id:              String,
      accessor:        T => A,
      header:          js.UndefOr[raw.mod.HeaderContext[T, A] => VdomNode] = js.undefined,
      cell:            raw.mod.CellContext[T, A] => VdomNode,
      footer:          js.UndefOr[raw.mod.HeaderContext[T, A] => VdomNode] = js.undefined,
      meta:            js.UndefOr[Any] = js.undefined,
      // Sorting
      enableMultiSort: js.UndefOr[Boolean] = js.undefined,
      enableSorting:   js.UndefOr[Boolean] = js.undefined,
      invertSorting:   js.UndefOr[Boolean] = js.undefined,
      sortDescFirst:   js.UndefOr[Boolean] = js.undefined,
      // sortUndefined:   js.UndefOr[false | -1 | 1] = js.undefined,
      sortingFn:       js.UndefOr[raw.mod.SortingFn[A]] = js.undefined
    ): Single[T, A] =
      Single(id,
             accessor,
             header,
             cell,
             footer,
             meta,
             enableMultiSort,
             enableSorting,
             invertSorting,
             sortDescFirst,
             //  sortUndefined,
             sortingFn
      )
