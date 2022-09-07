// Copyright (c) 2016-2022 Association of Universities for Research in Astronomy, Inc. (AURA)
// For license information see LICENSE or https://opensource.org/licenses/BSD-3-Clause

package lucuma.react.table

import cats.syntax.all.*
import reactST.{tanstackTableCore => raw}
import react.common.*
import japgolly.scalajs.react.*
import japgolly.scalajs.react.vdom.html_<^.*
import reactST.{tanstackReactTable => rawReact}
import reactST.{tanstackTableCore => raw}
import scalajs.js
import org.scalajs.dom.HTMLDivElement
import reactST.{tanstackReactVirtual => rawVirtualReact}
import reactST.{tanstackVirtualCore => rawVirtual}
import scala.scalajs.js.annotation.JSImport
import react.common.style.Css

trait VirtualOptions /*[S, I]*/ extends js.Object:
  val count: Int
  val getScrollElement: js.Function0[HTMLDivElement]
  val estimateSize: js.Function1[Int, Int]
  val overscan: js.UndefOr[Int]

@JSImport("@tanstack/react-virtual", "useVirtualizer")
@js.native
def useVirtualizerJS[T](options: VirtualOptions): rawVirtual.mod.Virtualizer[Any, Any] =
  js.native

final case class HTMLTable[T](table: raw.mod.Table[T], clazz: Css = Css.Empty)
    extends ReactFnProps(HTMLTable.component)

final case class HTMLTableVirtualized[T](
  table:          raw.mod.Table[T],
  containerClazz: Css = Css.Empty,
  clazz:          Css = Css.Empty
) extends ReactFnProps(HTMLTableVirtualized.component)

private def sortIndicator[T](col: raw.mod.Column[T, ?]): TagMod =
  col.getIsSorted().asInstanceOf[Boolean | String] match
    case sorted: String =>
      val index   = if (col.getSortIndex() > 0) (col.getSortIndex() + 1).toInt.toString else ""
      val ascDesc = if (sorted == "desc") "\u2191" else "\u2193"
      <.span(s" $index$ascDesc")
    case _              => TagMod.empty

private def render[T](
  table:         raw.mod.Table[T],
  rows:          js.Array[raw.mod.Row[T]],
  clazz:         Css = Css.Empty,
  paddingTop:    Option[Int] = none,
  paddingBottom: Option[Int] = none
) =
  <.table(clazz)(
    <.thead(
      table
        .getHeaderGroups()
        .map(headerGroup =>
          <.tr(^.key := headerGroup.id)(
            headerGroup.headers
              .map(header =>
                <.th(^.key     := header.id,
                     ^.colSpan := header.colSpan.toInt,
                     ^.width   := s"${header.getSize().toInt}px"
                )(
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
    <.tbody(paddingTop.filter(_ > 0).map(p => <.tr(<.td(^.height := s"${p}px"))).whenDefined)(
      rows
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
    )(
      paddingBottom.filter(_ > 0).map(p => <.tr(<.td(^.height := s"${p}px"))).whenDefined
    ),
    <.tfoot(
      table
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

object HTMLTable:
  private type Props[T] = HTMLTable[T]

  private def componentBuilder[T] =
    ScalaFnComponent[Props[T]](props =>
      render(props.table, props.table.getRowModel().rows, props.clazz)
    )

  private val component = componentBuilder[Any]

object HTMLTableVirtualized:
  private type Props[T] = HTMLTableVirtualized[T]

  // private type VirtualOptionsFacade[TScrollElement, TItemElement] =
  //   rawVirtual.mod.PartialKeys[
  //     rawVirtual.mod.VirtualizerOptions[TScrollElement, TItemElement],
  //     js.Function1[Any, Unit]
  //   ]

  // private def useVirtualJS[TScrollElement, TItemElement] =
  //   CustomHook.unchecked[VirtualOptions[TScrollElement, TItemElement],
  //                        rawVirtual.mod.Virtualizer[TScrollElement, TItemElement]
  //   ](opts =>
  //     rawVirtualReact.mod.useVirtualizer(
  //       opts.asInstanceOf[VirtualOptionsFacade[TScrollElement, TItemElement]]
  //     )
  //   )

  // private def useVirtualizerJS[T](options: VirtualOptions): rawVirtual.mod.Virtualizer[Any, Any] =
  //   ???

  private def componentBuilderVirtualized[T] =
    ScalaFnComponent
      .withHooks[Props[T]]
      .useRefToVdom[HTMLDivElement]
      .useMemoBy((_, _) => ())((props, ref) =>
        _ =>
          useVirtualizerJS(
            new VirtualOptions {
              override val count            = props.table.getRowModel().rows.length
              override val estimateSize     = _ => 24
              override val getScrollElement = () => ref.raw.current.asInstanceOf[HTMLDivElement]
              override val overscan         = 10
            }
          )
      )
      .render { (props, ref, virtualizer) =>
        val rows          = props.table.getRowModel().rows
        val virtualRows   = virtualizer.getVirtualItems()
        val totalSize     = virtualizer.getTotalSize().toInt
        val paddingTop    =
          if (virtualRows.length > 0) virtualRows.headOption.map(_.start.toInt).getOrElse(0) else 0
        val paddingBottom =
          if (virtualRows.length > 0)
            totalSize - virtualRows.lastOption.map(_.end.toInt).getOrElse(0)
          else 0

        <.div.withRef(ref)(props.containerClazz)(
          render(
            props.table,
            virtualRows.map(virtualItem => rows(virtualItem.index.toInt)),
            props.clazz,
            paddingTop.some,
            paddingBottom.some
          )
        )
      }

  private val component = componentBuilderVirtualized[Any]
