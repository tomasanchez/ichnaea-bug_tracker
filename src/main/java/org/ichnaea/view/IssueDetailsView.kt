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
import org.ichnaea.model.IssueStatus
import java.awt.FlowLayout
import java.awt.Font
import javax.swing.JPanel

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
            "fill, insets 0, debug",
            "0[100%]0",
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

            val assignee =
                Typography(
                    text = "<b>Assignee:</b>  ${it.assignee?.userName ?: "Unassigned"}",
                    size = 12f
                )

            val statusPanel = JPanel().also { statusPanel ->
                statusPanel.isOpaque = false
                statusPanel.layout = FlowLayout(FlowLayout.LEFT, 0, 0)
            }

            val statusLabel =
                Typography(
                    text = "<b>Status: </b>",
                    size = 12f
                )

            val status = Typography(
                text = it.getStatusLabel(),
                style = Font.BOLD,
                color = when (it.status) {
                    IssueStatus.IN_PROGRESS -> SemanticColor.PRIMARY
                    IssueStatus.TO_DO -> SemanticColor.SECONDARY
                    IssueStatus.BLOCKED -> SemanticColor.DANGER
                    IssueStatus.DONE -> SemanticColor.SUCCESS
                },
                size = 12f
            )

            val storyPoints = Typography(
                text = "<b>Story Points:</b> ${it.estimatedPoints}",
                size = 12f
            )


            statusPanel.add(statusLabel)
            statusPanel.add(status)

            containerPanel.add(breadCrumb, "align left, h 30!, growx, wrap")
            containerPanel.add(title, "align left, growx, wrap")
            containerPanel.add(description, "align left, growx, wrap")
            containerPanel.add(assignee, "align left, growx, wrap")
            containerPanel.add(statusPanel, "align left, aligny center, growx, wrap")
            containerPanel.add(storyPoints, "align left, growx, wrap")

        }

    }

    private fun body() {

    }
    // ---------------------------------------------------------------------------------------------
    // Internal Methods
    // ---------------------------------------------------------------------------------------------
}