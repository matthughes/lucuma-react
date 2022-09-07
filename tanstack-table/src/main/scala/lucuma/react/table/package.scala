// Copyright (c) 2016-2022 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package lucuma.react //.table

import reactST.{tanstackTableCore => raw}

package object table extends HooksApiExt:
// extension [T, A](cellCtx: raw.mod.CellContext[T, A])
//   def value: A = cellCtx.getValue().asInstanceOf[A]

// you won't believe it, but extension doesn't work
  implicit class CellContextOps[T, A](cellCtx: raw.mod.CellContext[T, A]):
    def value: A = cellCtx.getValue().asInstanceOf[A]
