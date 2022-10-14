package org.ichnaea.view

import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons
import org.ichnaea.core.mvc.view.View
import org.ichnaea.core.ui.button.Button
import org.ichnaea.core.ui.container.Toolbar
import org.ichnaea.core.ui.data.Table
import java.time.LocalDate
import java.time.LocalDateTime
import javax.swing.Box
import javax.swing.JScrollPane
import javax.swing.table.DefaultTableModel

@View
class ProjectsView : MenuView() {

    init {
        body()
        footer()
    }


    private fun body() {
        val table = table()
        set(table.tableHeader)
        table.addRow(arrayOf("Project 1", LocalDate.now(), LocalDateTime.now(), "In Progress"))
        table.addRow(arrayOf("Project 2", LocalDate.now(), LocalDateTime.now(), "Analysis"))
        set(table)
    }

    private fun footer() {
        set(Box.createVerticalStrut(20))
        val toolbar = Toolbar()
        set(
            "createProject",
            Button(text = "New Project", icon = GoogleMaterialDesignIcons.ADD_TO_QUEUE),
        )
        set(toolbar)
    }


    private fun table(): Table {

        val table = Table()
        val scrollPanel = JScrollPane()

        table.model = object : DefaultTableModel(
            arrayOf(), arrayOf(
                "Project Name", "Created", "Updated", "Status"
            )
        ) {
            var canEdit = booleanArrayOf(
                false, false, false, false, false
            )

            override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean {
                return canEdit[columnIndex]
            }
        }
        scrollPanel.setViewportView(table)
        table.fixTable(scrollPanel)
        return table
    }


}
