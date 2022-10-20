package org.ichnaea.view

import net.miginfocom.swing.MigLayout
import org.ichnaea.core.mvc.view.UIView
import org.ichnaea.core.ui.semantic.SemanticColor
import org.ichnaea.core.ui.text.Title
import org.ichnaea.core.ui.text.TitleLevel
import org.ichnaea.core.ui.text.Typography
import org.ichnaea.model.Project
import java.awt.Dimension
import javax.swing.Box

@UIView
class ProjectDetailsView : SideView() {

    init {
        onBeforeRendering()
    }

    private fun onBeforeRendering() {
        containerPanel.layout = MigLayout("debug")
        header()
    }

    override fun repaint() {
        containerPanel.removeAll()
        super.repaint()
        onBeforeRendering()
    }

    // ---------------------------------------------------------------------------------------------
    // View Drawing
    // ---------------------------------------------------------------------------------------------

    fun header() {

        val project: Project? = model["project"] as Project?
        project?.let {

            val title = Title(text = it.name, level = TitleLevel.H3)
            containerPanel.add(title, "align center")
            containerPanel.add(Box.createRigidArea(Dimension(200, 0)), "align center, w 300!, growx")
            containerPanel.add(Typography(text = it.creationDate), "wrap, align center")

            val description =
                Typography(text = it.description, color = SemanticColor.DARK.brighter())
            containerPanel.add(description, "align right, wrap")
        }

    }


}