package org.ichnaea.view

import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons
import net.miginfocom.swing.MigLayout
import org.ichnaea.core.mvc.view.UIView
import org.ichnaea.core.ui.container.TabContainer
import org.ichnaea.core.ui.icon.GoogleIconFactory
import org.ichnaea.core.ui.semantic.SemanticColor
import org.ichnaea.core.ui.text.Title
import org.ichnaea.core.ui.text.TitleLevel
import org.ichnaea.core.ui.text.Typography
import org.ichnaea.model.Project
import java.awt.Dimension
import javax.swing.Box
import javax.swing.JPanel

@UIView
class ProjectDetailsView : SideView() {

    private var project: Project? = null

    private lateinit var tabs: TabContainer

    init {
        onBeforeRendering()
    }

    private fun onBeforeRendering() {
        containerPanel.layout = MigLayout(
            "fill",
            "0[]10[100%, fill]0",
            "0[fill, top]0"
        )
        header()
        body()
    }

    override fun repaint() {
        containerPanel.removeAll()
        super.repaint()
        onBeforeRendering()
    }

    // ---------------------------------------------------------------------------------------------
    // View Drawing
    // ---------------------------------------------------------------------------------------------

    private fun header() {

        project = model["project"] as Project?

        project?.let {

            val title = Title(text = it.name, level = TitleLevel.H3)
            containerPanel.add(title, "align left, wrap")
            val description =
                Typography(text = it.description, color = SemanticColor.DARK.brighter())
            containerPanel.add(description, "align right, growx, wrap")
            val date = Typography(text = "<b>Created at:</b> ${it.creationDate}", size = 12f)
            containerPanel.add(date, "align right, growx, wrap")
        }

    }

    private fun body() {

        containerPanel.add(
            Box.createRigidArea(
                Dimension(200, 50)
            ),
            "align center, w 300!, growx, wrap"
        )

        createTab()
        containerPanel.add(tabs, "align center, growx,  spanx, wrap")

    }

    // ---------------------------------------------------------------------------------------------
    // Components
    // ---------------------------------------------------------------------------------------------

    private fun createTab() {
        tabs = TabContainer()
        issueTab()
        tabs.addTab("Members", JPanel())
    }

    private fun issueTab() {
        val tab = JPanel().also { it.background = SemanticColor.LIGHT }
        val icon = GoogleIconFactory.build(
            name = GoogleMaterialDesignIcons.BUG_REPORT,
            color = SemanticColor.DARK,
            size = 24f
        )
        tabs.addTab("Issues", icon, tab)
    }


}