// Copyright (c) 2016-2022 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package lucuma.react.table

import reactST.{tanstackTableCore => raw}
import react.common.ReactFnProps
import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import react.common.ReactFnProps
import reactST.tanstackReactTable.mod.flexRender
import reactST.tanstackTableCore.mod.HeaderContext
import reactST.tanstackTableCore.mod.CellContext
import reactST.tanstackReactTable.mod.Renderable

final case class HTMLTable[T](table: raw.mod.Table[T]) extends ReactFnProps(HTMLTable.component)

object HTMLTable:
  private type Props[T] = HTMLTable[T]

  private def componentBuilder[T] =
    ScalaFnComponent[Props[T]](props =>
      <.table(
        <.thead(
          props.table
            .getHeaderGroups()
            .map(headerGroup =>
              <.tr(^.key := headerGroup.id)(
                headerGroup.headers
                  .map(header =>
                    <.th(^.key := header.id, ^.colSpan := header.colSpan.toInt)(
                      TagMod.unless(header.isPlaceholder)(
                        <.div(
                          header.column
                            .getToggleSortingHandler()
                            .map(handler =>
                              ^.onClick ==> { e =>
                                Callback(handler(e))
                              }
                            )
                            .whenDefined
                        )(
                          flexRender(
                            header.column.columnDef
                              .asInstanceOf[HeaderContext[T, Any]]
                              .header
                              .asInstanceOf[Renderable[HeaderContext[T, Any]]],
                            header.getContext().asInstanceOf[HeaderContext[T, Any]]
                          )
                        )
                      )
                    )
                  )
                  .toArray: _*
              )
            )
            .toArray: _*
        ),
        <.tbody(
          props.table
            .getRowModel()
            .rows
            .map(row =>
              <.tr(^.key := row.id)(
                row
                  .getVisibleCells()
                  .map(cell =>
                    <.td(^.key := cell.id)(
                      flexRender(
                        cell.column.columnDef.cell
                          .asInstanceOf[Renderable[CellContext[T, Any]]],
                        cell.getContext().asInstanceOf[CellContext[T, Any]]
                      )
                    )
                  )
                  .toArray: _*
              )
            )
            .toArray: _*
        ),
        <.tfoot(
          props.table
            .getFooterGroups()
            .map(footerGroup =>
              <.tr(^.key := footerGroup.id)(
                footerGroup.headers.map { footer =>
                  <.th(^.key := footer.id, ^.colSpan := footer.colSpan.toInt)(
                    TagMod.unless(footer.isPlaceholder)(
                      flexRender(
                        footer.column.columnDef.footer
                          .asInstanceOf[Renderable[HeaderContext[T, Any]]],
                        footer.getContext()
                      )
                    )
                  )
                }.toArray: _*
              )
            )
            .toArray: _*
        )
      )
    )

  protected val component = componentBuilder[Any]
