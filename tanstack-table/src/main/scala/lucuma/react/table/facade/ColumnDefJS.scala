// Copyright (c) 2016-2022 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package lucuma.react.table.facade

import reactST.{tanstackTableCore => raw}
import japgolly.scalajs.react.facade.React.Node

import scalajs.js
import reactST.tanstackTableCore.mod.ColumnMeta

trait ColumnDefJS[T, A] extends js.Object:
  var id: String
  var accessorFn: js.UndefOr[js.Function1[T, A]]
  var cell: js.UndefOr[js.Function1[raw.mod.CellContext[T, A], Node]]
  var columns: js.UndefOr[js.Array[ColumnDefJS[T, Any]]]
  var header: js.UndefOr[js.Function1[raw.mod.HeaderContext[T, A], Node]]
  var footer: js.UndefOr[js.Function1[raw.mod.HeaderContext[T, A], Node]]
  var meta: js.UndefOr[Any]
