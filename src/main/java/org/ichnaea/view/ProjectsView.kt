package org.ichnaea.view

import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons
import org.ichnaea.core.mvc.view.View
import org.ichnaea.core.ui.button.Button
import org.ichnaea.core.ui.container.Toolbar
import org.ichnaea.core.ui.data.Table
import org.ichnaea.core.ui.text.Title
import org.ichnaea.core.ui.text.TitleLevel
import java.awt.Dimension
import javax.swing.Box
import javax.swing.JScrollPane
import javax.swing.table.DefaultTableModel

@View
class ProjectsView : SideView() {

    private val scrollPanel = JScrollPane()

    init {
        body()
        footer()
    }

    private fun body() {
        set(Box.createRigidArea(Dimension(0, 20)))
        val title = Title(text = "Projects", level = TitleLevel.H2)
        set(title)
        set(Box.createRigidArea(Dimension(0, 20)))
        val table = table()
        set(scrollPanel)
        addToModel("projectsTable", table)
    }

    private fun footer() {
        containerPanel.add(Box.createRigidArea(Dimension(0, 35)))
        val toolbar = Toolbar()
        toolbar.add(Button(text = "New Project", icon = GoogleMaterialDesignIcons.ADD))
        containerPanel.add(toolbar)
    }


    private fun table(): Table {
        val table = Table(active = true, showHeader = false)

        table.model = object : DefaultTableModel(
            arrayOf(), arrayOf(
                "Name",
            )
        ) {
            var canEdit = booleanArrayOf(
                false,
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
