package org.ichnaea.view

import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons
import org.ichnaea.core.mvc.view.UIView
import org.ichnaea.core.ui.button.Button
import org.ichnaea.core.ui.container.Toolbar
import org.ichnaea.core.ui.semantic.SemanticColor
import org.ichnaea.core.ui.text.Title
import org.ichnaea.core.ui.text.TitleLevel
import java.awt.Dimension
import javax.swing.Box
import javax.swing.JScrollPane

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
        val table = table(scrollPanel)
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


}