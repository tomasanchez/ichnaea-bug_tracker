package org.ichnaea.view

import org.ichnaea.core.mvc.view.UIView
import org.ichnaea.core.ui.container.TransparentPanel
import org.ichnaea.core.ui.data.Table
import org.ichnaea.core.ui.text.Title
import org.ichnaea.core.ui.text.TitleLevel
import javax.swing.JScrollPane
import javax.swing.table.DefaultTableModel

@UIView
class ProjectReportView : SideView() {

    private lateinit var table: Table

    private val columns = arrayOf(
        "Name",
        "To do", "Blocked", "In progress", "Done", "Total",
        "Estimated", "Real"
    )

    init {
        initComponents()
        header()
        body()
        footer()
    }

    private fun initComponents() {
        reportTable()
    }


    // ---------------------------------------------------------------------------------------------
    // View Structure
    // ---------------------------------------------------------------------------------------------


    private fun header() {
        val title = Title(text = "Project Report", level = TitleLevel.H2)
        model["title"] = title
        containerPanel.add(title, "align center, wrap")
    }

    private fun body() {
        val tablePanel = JScrollPane()
        tablePanel.setViewportView(table)
        table.fixTable(tablePanel)

        containerPanel.add(TransparentPanel(), " h 35!, wrap")
        containerPanel.add(table.tableHeader, "align center, grow, span")
        containerPanel.add(tablePanel, "align center, grow, span")
    }

    private fun footer() {
    }

    // ---------------------------------------------------------------------------------------------
    // Internal methods
    // ---------------------------------------------------------------------------------------------
    private fun reportTable() {

        table = Table(
            active = false,
        )

        table.model = object : DefaultTableModel(
            arrayOf(), columns
        ) {
            override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean {
                return false
            }
        }

        model["table"] = table
    }

}