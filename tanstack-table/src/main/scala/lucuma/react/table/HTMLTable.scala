// Copyright (c) 2016-2022 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package lucuma.react.table

import reactST.{tanstackTableCore => raw}
import react.common.ReactFnProps
import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import react.common.ReactFnProps
import reactST.{tanstackReactTable => rawReact}
import reactST.{tanstackTableCore => raw}

final case class HTMLTable[T](table: raw.mod.Table[T]) extends ReactFnProps(HTMLTable.component)

object HTMLTable:
  private type Props[T] = HTMLTable[T]

  private def sortIndicator[T](col: raw.mod.Column[T, ?]): TagMod =
    col.getIsSorted().asInstanceOf[Boolean | String] match
      case sorted: String =>
        val index   = if (col.getSortIndex() > 0) (col.getSortIndex() + 1).toInt.toString else ""
        val ascDesc = if (sorted == "desc") "\u2191" else "\u2193"
        <.span(s" $index$ascDesc")
      case _              => TagMod.empty

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
                          rawReact.mod.flexRender(
                            header.column.columnDef
                              .asInstanceOf[raw.mod.HeaderContext[T, Any]]
                              .header
                              .asInstanceOf[rawReact.mod.Renderable[raw.mod.HeaderContext[T, Any]]],
                            header.getContext().asInstanceOf[raw.mod.HeaderContext[T, Any]]
                          ),
                          sortIndicator(header.column)
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
                      rawReact.mod.flexRender(
                        cell.column.columnDef.cell
                          .asInstanceOf[rawReact.mod.Renderable[raw.mod.CellContext[T, Any]]],
                        cell.getContext().asInstanceOf[raw.mod.CellContext[T, Any]]
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
                      rawReact.mod.flexRender(
                        footer.column.columnDef.footer
                          .asInstanceOf[rawReact.mod.Renderable[raw.mod.HeaderContext[T, Any]]],
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
