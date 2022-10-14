package org.ichnaea.core.ui.data

import org.ichnaea.core.ui.semantic.SemanticColor
import org.ichnaea.core.ui.text.TableHeader
import org.ichnaea.core.ui.text.Typography
import java.awt.Color
import java.awt.Component
import java.awt.Font
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTable
import javax.swing.ListSelectionModel.SINGLE_SELECTION
import javax.swing.table.DefaultTableCellRenderer
import javax.swing.table.DefaultTableModel


class Table(
    selectionMode: Int = SINGLE_SELECTION,
) : JTable() {

    init {
        showHorizontalLines = true
        showVerticalLines = true
        gridColor = SemanticColor.LIGHT
        rowHeight = 60
        setSelectionMode(selectionMode)

        tableHeader.reorderingAllowed = false
        tableHeader.defaultRenderer = object : DefaultTableCellRenderer() {
            override fun getTableCellRendererComponent(
                jtable: JTable,
                o: Any,
                bln: Boolean,
                bln1: Boolean,
                i: Int,
                i1: Int
            ): Component = TableHeader(o.toString())
        }

        setDefaultRenderer(Any::class.java, object : DefaultTableCellRenderer() {
            override fun getTableCellRendererComponent(
                jtable: JTable,
                o: Any,
                selected: Boolean,
                bln1: Boolean,
                i: Int,
                i1: Int
            ): Component {
                border = noFocusBorder
                return Typography(o.toString(), style = if (selected) Font.BOLD else Font.PLAIN)
            }
        })

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

        val p = JPanel()
        p.background = SemanticColor.LIGHT
        scroll.setCorner(JScrollPane.UPPER_RIGHT_CORNER, p)
    }


}