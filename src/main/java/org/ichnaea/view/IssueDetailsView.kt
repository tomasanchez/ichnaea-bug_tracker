package org.ichnaea.view

import net.miginfocom.swing.MigLayout
import org.ichnaea.core.mvc.view.UIView
import org.ichnaea.core.ui.container.TransparentPanel
import org.ichnaea.core.ui.navigation.BreadCrumb
import org.ichnaea.core.ui.semantic.SemanticColor
import org.ichnaea.core.ui.text.Link
import org.ichnaea.core.ui.text.Title
import org.ichnaea.core.ui.text.TitleLevel
import org.ichnaea.core.ui.text.Typography
import org.ichnaea.model.Issue
import org.ichnaea.model.IssueStatus
import java.awt.Font

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
            "[][grow, fill]50[][grow,fill]",
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


            val assigneeLabel = Typography(text = "Assignee: ", size = 12f, style = Font.BOLD)
            val assignee =
                Typography(
                    text = it.assignee?.userName ?: "Unassigned",
                    color = it.assignee?.let { SemanticColor.PRIMARY } ?: SemanticColor.DARK,
                    size = 12f
                )


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

            val storyPointsLabel = Typography(text = "Estimate: ", size = 12f, style = Font.BOLD)
            val storyPoints = Typography(
                text = it.estimatedPoints.toString(),
                size = 12f
            )

            val realLabel = Typography(text = "Real: ", size = 12f, style = Font.BOLD)
            val real = Typography(
                text = it.realPoints.toString(),
                color = it.realPoints?.let { points ->
                    when (it.estimatedPoints - points) {
                        0 -> SemanticColor.SECONDARY
                        in 0..Int.MAX_VALUE -> SemanticColor.SUCCESS
                        else -> SemanticColor.DANGER
                    }
                } ?: SemanticColor.DARK,
                size = 12f
            )


            containerPanel.add(breadCrumb, "align left, h 30!, wrap")

            containerPanel.add(title, "align left, wrap")

            containerPanel.add(
                Title(
                    text = "Details",
                    level = TitleLevel.H4,
                    color = SemanticColor.SECONDARY
                ), "align left, span 2"
            )
            containerPanel.add(
                Title(text = "People", level = TitleLevel.H4, color = SemanticColor.SECONDARY),
                "w 75!, wrap"
            )

            containerPanel.add(statusLabel, "w 75!, left")
            containerPanel.add(status, "align left")
            containerPanel.add(assigneeLabel, "w 75!")
            containerPanel.add(assignee, "wrap")


            containerPanel.add(storyPointsLabel, "w 75!")
            containerPanel.add(storyPoints, "wrap")
            it.realPoints?.let {
                containerPanel.add(realLabel, "w 75!")
                containerPanel.add(real, "wrap")
            }


        }

    }

    private fun body() {
        issue?.let {

            val descriptionTitle = Title(text = "Description", level = TitleLevel.H4)

            val description =
                Typography(
                    text = it.description,
                    color = SemanticColor.DARK.brighter()
                )

            containerPanel.add(TransparentPanel(), "h 75!,growx, span, wrap")
            containerPanel.add(descriptionTitle, "align left, wrap")
            containerPanel.add(description, "align left, span, wrap")
        }

    }
    // ---------------------------------------------------------------------------------------------
    // Internal Methods
    // ---------------------------------------------------------------------------------------------
}