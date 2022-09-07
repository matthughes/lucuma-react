// Copyright (c) 2016-2022 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package lucuma.react.table

import japgolly.scalajs.react.*
import reactST.{tanstackTableCore => raw}
// import reactST.tanstackTableCore.mod.*
// import reactST.tanstackTableCore.anon.*
import reactST.{tanstackReactTable => rawReact}

import scalajs.js
import scalajs.js.JSConverters.*

case class TableOptions[T](
  columns:              Reusable[List[ColumnDef[T, ?]]],
  data:                 Reusable[List[T]],
  getCoreRowModel:      js.UndefOr[raw.mod.Table[Any] => js.Function0[raw.mod.RowModel[Any]]] =
    js.undefined,
  onStateChange:        js.UndefOr[raw.mod.Updater[raw.mod.TableState] => Callback] = js.undefined,
  renderFallbackValue:  js.UndefOr[Any] = js.undefined,
  state:                js.UndefOr[raw.anon.PartialTableState] = js.undefined,
  initialState:         js.UndefOr[raw.mod.InitialTableState] = js.undefined,
  // Sorting
  enableSorting:        js.UndefOr[Boolean] = js.undefined,
  enableMultiSort:      js.UndefOr[Boolean] = js.undefined,
  enableSortingRemoval: js.UndefOr[Boolean] = js.undefined,
  enableMultiRemove:    js.UndefOr[Boolean] = js.undefined,
  getSortedRowModel:    js.UndefOr[raw.mod.Table[Any] => js.Function0[raw.mod.RowModel[Any]]] =
    js.undefined,
  isMultiSortEvent:     js.UndefOr[js.Function1[ /* e */ Any, Boolean]] = js.undefined,
  manualSorting:        js.UndefOr[Boolean] = js.undefined,
  maxMultiSortColCount: js.UndefOr[Double] = js.undefined,
  onSortingChange:      js.UndefOr[raw.mod.OnChangeFn[raw.mod.SortingState]] = js.undefined,
  sortDescFirst:        js.UndefOr[Boolean] = js.undefined,
  // Expanding
  enableExpanding:      js.UndefOr[Boolean] = js.undefined,
  getExpandedRowModel:  js.UndefOr[raw.mod.Table[Any] => js.Function0[raw.mod.RowModel[Any]]] =
    js.undefined,
  getSubRows:           js.UndefOr[T => List[T]] = js.undefined // Undocumented!
)
// There's no toJS method here: JS version is constructed manually in the Hook with memoized columns and data.
