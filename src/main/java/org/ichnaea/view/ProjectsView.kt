package org.ichnaea.view

import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons
import org.ichnaea.core.mvc.view.UIView
import org.ichnaea.core.ui.button.Button
import org.ichnaea.core.ui.container.Toolbar
import org.ichnaea.core.ui.text.Title
import org.ichnaea.core.ui.text.TitleLevel
import java.awt.Dimension
import javax.swing.Box
import javax.swing.JScrollPane


@UIView
class ProjectsView : SideView() {

    private val scrollPanel = JScrollPane()

    init {
        body()
        footer()
    }

    private fun body() {
        set(Box.createRigidArea(Dimension(0, 20)))
        val title = Title(text = "Projects", level = TitleLevel.H2)
        set("viewTitle", title)
        set(Box.createRigidArea(Dimension(0, 20)))
        val table = table(scrollPanel)
        set(scrollPanel)
        addToModel("projectsTable", table)
    }

    private fun footer() {
        containerPanel.add(Box.createRigidArea(Dimension(0, 35)))

        val toolbar = Toolbar()
        val button = Button(text = "New Project", icon = GoogleMaterialDesignIcons.ADD)
        model["newProjectButton"] = button
        toolbar.add(button)
        containerPanel.add(toolbar)

    }

}
