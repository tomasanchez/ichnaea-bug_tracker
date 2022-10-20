package org.ichnaea.view

import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons
import org.ichnaea.core.mvc.view.UIView
import org.ichnaea.core.ui.button.Button
import org.ichnaea.core.ui.container.Toolbar
import org.ichnaea.core.ui.data.Table
import org.ichnaea.core.ui.semantic.SemanticColor
import org.ichnaea.core.ui.text.Title
import org.ichnaea.core.ui.text.TitleLevel
import java.awt.Dimension
import javax.swing.Box
import javax.swing.JScrollPane
import javax.swing.table.DefaultTableModel

@UIView
class UsersView : SideView() {

    private val scrollPanel = JScrollPane()

    init {
        header()
        body()
        footer()
    }


    // ---------------------------------------------------------------------------------------------
    // View Drawing
    // ---------------------------------------------------------------------------------------------

    private fun header() {
        val title = Title(text = "Users", level = TitleLevel.H2)
        set("viewTitle", title)
    }

    private fun body() {
        set(Box.createRigidArea(Dimension(0, 20)))
        val table = table()
        set(scrollPanel)
        addToModel("usersTable", table)
    }

    private fun footer() {
        containerPanel.add(Box.createRigidArea(Dimension(0, 35)))
        val toolbar = Toolbar()
        val registerButton = Button(
            text = "Register User",
            icon = GoogleMaterialDesignIcons.PERSON_ADD,
            color = SemanticColor.PRIMARY
        )
        toolbar.add(
            registerButton
        )
        model["registerButton"] = registerButton
        containerPanel.add(toolbar)
    }

    // ---------------------------------------------------------------------------------------------
    // Components
    // ---------------------------------------------------------------------------------------------

    private fun table(): Table {

        val table = Table(
            active = true,
            showHeader = false
        )

        table.model = object : DefaultTableModel(
            arrayOf(), arrayOf(
                "Id", "Name",
            )
        ) {
            override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean {
                return false
            }
        }

        table.columnModel.getColumn(0).width = 0
        table.columnModel.getColumn(0).maxWidth = 0

        scrollPanel.setViewportView(table)
        table.fixTable(scrollPanel)
        return table
    }

}