package org.ichnaea.core.ui.data

import org.ichnaea.core.ui.semantic.SemanticColor
import org.ichnaea.core.ui.text.TableHeader
import org.ichnaea.core.ui.text.Typography
import java.awt.Color
import java.awt.Component
import java.awt.Font
import java.awt.Point
import java.awt.event.MouseEvent
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTable
import javax.swing.ListSelectionModel.SINGLE_SELECTION
import javax.swing.border.EmptyBorder
import javax.swing.table.DefaultTableCellRenderer
import javax.swing.table.DefaultTableModel


class Table(
    selectionMode: Int = SINGLE_SELECTION,
    active: Boolean = false,
    showHeader: Boolean = true,
) : JTable() {

    init {
        showHorizontalLines = true
        showVerticalLines = true
        gridColor = SemanticColor.LIGHT
        rowHeight = 60
        rowSelectionAllowed = active
        setSelectionMode(selectionMode)

        tableHeader.reorderingAllowed = false
        tableHeader.defaultRenderer = object : DefaultTableCellRenderer() {
            override fun getTableCellRendererComponent(
                jtable: JTable,
                value: Any,
                isSelected: Boolean,
                hasFocus: Boolean,
                row: Int,
                column: Int,
            ): Component = TableHeader(value.toString())
        }

        setDefaultRenderer(Any::class.java, object : DefaultTableCellRenderer() {
            override fun getTableCellRendererComponent(
                jtable: JTable,
                value: Any,
                isSelected: Boolean,
                hasFocus: Boolean,
                row: Int,
                column: Int,
            ): Component {

                val component = Typography(
                    value.toString(),
                    style = if (isSelected) Font.BOLD else Font.PLAIN,
                )

                border = noFocusBorder

                if (active) {
                    component.isOpaque = true
                    component.border = noFocusBorder
                    component.background = if (isSelected) SemanticColor.LIGHT else Color.WHITE
                }

                return component
            }
        })

        if (active) {
            attachActiveRowsEffect()
        }


        if (!showHeader)
            tableHeader = null
    }

    fun addRow(row: Array<Any>) {
        val model = model as DefaultTableModel
        model.addRow(row)
    }

    fun fixTable(scroll: JScrollPane) {
        scroll.border = null
        scroll.verticalScrollBar = org.ichnaea.core.ui.container.ScrollBar()
        scroll.verticalScrollBar.background = Color.WHITE
        scroll.viewport.background = Color.WHITE
        scroll.background = Color.WHITE

        val p = JPanel()
        p.background = SemanticColor.LIGHT
        p.isOpaque = true
        scroll.setCorner(JScrollPane.UPPER_RIGHT_CORNER, p)
        scroll.border = EmptyBorder(5, 5, 5, 5)
    }

    private fun attachActiveRowsEffect() {
        addMouseMotionListener(object : java.awt.event.MouseMotionAdapter() {

            private var hoveredRow = -1

            override fun mouseDragged(e: MouseEvent?) {
                hoveredRow = -1
                cursor = java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR)
                this@Table.selectionModel.clearSelection()
            }

            override fun mouseMoved(e: MouseEvent) {
                val p: Point = e.point
                hoveredRow = this@Table.rowAtPoint(p)

                if (this@Table.model.rowCount > 0) {
                    this@Table.addRowSelectionInterval(
                        hoveredRow,
                        hoveredRow
                    )
                    cursor = java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)
                }

                this@Table.repaint()
            }
        })
    }

    fun onRowClick(idConsumer: (id: Number) -> Unit) {
        addMouseListener(object : java.awt.event.MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                val p: Point = e.point
                val row = this@Table.rowAtPoint(p)
                val col = this@Table.columnAtPoint(p)
                if (row >= 0 && col >= 0) {
                    this@Table.selectionModel.clearSelection()
                    val id = this@Table.model.getValueAt(row, 0)
                    idConsumer(id as Long)
                }
            }
        })
    }

    fun clear() {
        val model = model as DefaultTableModel
        model.rowCount = 0
    }

}