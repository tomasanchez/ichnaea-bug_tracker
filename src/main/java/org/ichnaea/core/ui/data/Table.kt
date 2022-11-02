package org.ichnaea.core.ui.data

import org.ichnaea.core.ui.semantic.SemanticColor
import org.ichnaea.core.ui.text.TableHeader
import org.ichnaea.core.ui.text.Typography
import java.awt.Color
import java.awt.Component
import java.awt.Font
import java.awt.Point
import java.awt.event.MouseEvent
import javax.swing.JComponent
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

                val component =
                    when (value) {
                        is JComponent -> value
                        else -> Typography(
                            value.toString(),
                            style = if (isSelected) Font.BOLD else Font.PLAIN,
                        )
                    }

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


    /**
     * Forces a width for the given column.
     *
     * @param column The column index.
     * @param width The width in pixels.
     */
    fun setColumnWidth(column: Int, width: Int) {
        getColumnModel().getColumn(column).preferredWidth = width
        getColumnModel().getColumn(column).maxWidth = width
        getColumnModel().getColumn(column).minWidth = width
    }

    /**
     * Adds a row to the table.
     * If an element is out of JComponent, it will be displayed as it is.
     * Otherwise, it will be converted to a Typography.
     *
     * @param row an array of any non-null elements
     */
    fun addRow(row: Array<Any>) {
        val model = model as DefaultTableModel
        model.addRow(row)
    }

    /**
     * Fix a table to a scroll pane
     *
     * @param scroll container which holds the table
     */
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

    /**
     * Event Handler for when a row is selected in the table.
     *
     * @param idConsumer the function to call when a row is selected
     */
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

    /**
     * Event handler for a specific column click.
     *
     * @param cell index of the column
     * @param idConsumer function to be called with the id of the clicked row
     */
    fun onCellClick(cell: Int, idConsumer: (id: Number) -> Unit) {
        addMouseListener(object : java.awt.event.MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                val p: Point = e.point
                val row = this@Table.rowAtPoint(p)
                val col = this@Table.columnAtPoint(p)
                if (row >= 0 && col == cell) {
                    this@Table.selectionModel.clearSelection()
                    val id = this@Table.model.getValueAt(row, 0)
                    idConsumer(id as Long)
                }
            }
        })
    }

    /**
     * Removes al rows of a table.
     *
     */
    fun clear() {
        val model = model as DefaultTableModel
        model.rowCount = 0
    }

}