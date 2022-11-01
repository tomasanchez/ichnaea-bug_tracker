package org.ichnaea.view

import net.miginfocom.swing.MigLayout
import org.ichnaea.core.mvc.view.UIView
import org.ichnaea.core.ui.navigation.BreadCrumb
import org.ichnaea.core.ui.semantic.SemanticColor
import org.ichnaea.core.ui.text.Link
import org.ichnaea.core.ui.text.Title
import org.ichnaea.core.ui.text.TitleLevel
import org.ichnaea.core.ui.text.Typography
import org.ichnaea.model.Issue

@UIView
class IssueDetailsView : SideView() {

    private var issue: Issue? = null

    private val homeLink = Link(
        text = "Home",
        tooltip = "Go to the home page",
    )

    private val projectLink = Link(
        text = "Project",
        tooltip = "Go to project details",
    )

    // ---------------------------------------------------------------------------------------------
    // View Drawing
    // ---------------------------------------------------------------------------------------------

    init {
        model["homeLink"] = homeLink
        model["projectLink"] = projectLink
    }


    private fun onBeforeRendering() {
        containerPanel.layout = MigLayout(
            "fill, insets 0",
            "0[100%]0",
            "0[fill]0"
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
    // Components
    // ---------------------------------------------------------------------------------------------

    private fun header() {

        issue = model["issue"] as Issue?

        issue?.let {

            val breadCrumb =
                BreadCrumb(
                    mutableListOf(
                        homeLink, projectLink,
                        Typography(text = issue!!.id.toString())
                    )
                )

            val title =
                Title(
                    text = it.title,
                    level = TitleLevel.H3
                )

            val description =
                Typography(
                    text = it.description,
                    color = SemanticColor.DARK.brighter()
                )

            val assignee = it.assignee?.let { assignee ->
                Typography(
                    text = "<b>Assignee:</b> ${assignee.userName}",
                    size = 12f
                )
            } ?: Typography(
                text = "<b>Assignee:</b> Unassigned",
                size = 12f
            )

            containerPanel.add(breadCrumb, "align left, h 30!, growx, wrap")
            containerPanel.add(title, "align left, growx, wrap")
            containerPanel.add(description, "align right, growx, wrap")
            containerPanel.add(assignee, "align right, growx, wrap")
        }

    }

    private fun body() {

    }
    // ---------------------------------------------------------------------------------------------
    // Internal Methods
    // ---------------------------------------------------------------------------------------------
}