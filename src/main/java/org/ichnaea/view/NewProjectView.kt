package org.ichnaea.view

import net.miginfocom.swing.MigLayout
import org.ichnaea.core.mvc.view.UIView
import org.ichnaea.core.ui.button.Button
import org.ichnaea.core.ui.container.ScrollContainer
import org.ichnaea.core.ui.container.Toolbar
import org.ichnaea.core.ui.form.TextArea
import org.ichnaea.core.ui.form.TextField
import org.ichnaea.core.ui.semantic.SemanticColor
import org.ichnaea.core.ui.text.Title
import org.ichnaea.core.ui.text.TitleLevel
import java.awt.Component
import java.awt.Dimension
import javax.swing.Box

@UIView
class NewProjectView : SideView() {

    init {

        containerPanel.layout = MigLayout(
            "fill",
        )

        header()
        body()
        footer()
    }

    // ---------------------------------------------------------------------------------------------
    // View Drawing
    // ---------------------------------------------------------------------------------------------

    private fun header() {
        containerPanel.add(Title(text = "Start a new Project", level = TitleLevel.H2), "align center, wrap")
    }

    private fun body() {
        containerPanel.add(Box.createRigidArea(Dimension(0, 20)), "wrap")

        model["nameField"] = TextField(label = "Name")
        containerPanel.add(model["nameField"] as Component, "align center, wrap")
        containerPanel.add(Box.createRigidArea(Dimension(0, 10)), "wrap")

        model["codeField"] = TextField(label = "Code")
        containerPanel.add(model["codeField"] as Component, "align center, wrap")
        containerPanel.add(Box.createRigidArea(Dimension(0, 10)), "wrap")

        val descriptionScroll = ScrollContainer()
        val descriptionArea = TextArea(label = "Description", scroll = descriptionScroll)
        containerPanel.add(descriptionScroll, "align center, wrap")
        model["descriptionArea"] = descriptionArea
    }

    private fun footer() {
        containerPanel.add(Box.createRigidArea(Dimension(0, 35)), "wrap")
        val footerPanel = Toolbar()
        model["cancelButton"] = Button(text = "Cancel", color = SemanticColor.DANGER)
        footerPanel.add(model["cancelButton"] as Component)
        model["createButton"] = Button(text = "Create Project")
        footerPanel.add(model["createButton"] as Component)
        containerPanel.add(footerPanel, "align center, wrap")
    }

    // ---------------------------------------------------------------------------------------------
    // Components
    // ---------------------------------------------------------------------------------------------

}