package org.ichnaea.view

import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons
import net.miginfocom.swing.MigLayout
import org.ichnaea.core.mvc.view.UIView
import org.ichnaea.core.ui.button.Button
import org.ichnaea.core.ui.container.TransparentPanel
import org.ichnaea.core.ui.navigation.BreadCrumb
import org.ichnaea.core.ui.semantic.SemanticColor
import org.ichnaea.core.ui.text.Link
import org.ichnaea.core.ui.text.Title
import org.ichnaea.core.ui.text.TitleLevel
import org.ichnaea.core.ui.text.Typography
import org.ichnaea.form.IssueForm
import org.ichnaea.model.Issue
import org.ichnaea.model.IssueStatus
import java.awt.FlowLayout
import java.awt.Font
import java.awt.event.ActionEvent
import javax.swing.JPanel

@UIView
class IssueDetailsView : SideView() {

    private var issue: Issue? = null
    private var issueEditId: Long? = null

    private val homeLink = Link(
        text = "Home",
        tooltip = "Go to the home page",
    )

    private val projectLink = Link(
        text = "Project",
        tooltip = "Go to project details",
    )

    private var edit = false

    private val editButton =
        Button(text = "Edit", icon = GoogleMaterialDesignIcons.EDIT).also { it.onClick(::toggleEdit) }

    private val deleteButton =
        Button(text = "Delete", icon = GoogleMaterialDesignIcons.DELETE, color = SemanticColor.DARK)

    private val cancelButton =
        Button(
            text = "Cancel",
            color = SemanticColor.SECONDARY,
        ).also { it.onClick(::toggleEdit) }

    private val saveButton = Button(text = "Save", icon = GoogleMaterialDesignIcons.SAVE)

    private val actions = IssueActions()

    class IssueActions {
        val toDo = Button(
            text = "To do",
            icon = GoogleMaterialDesignIcons.CHECK_BOX_OUTLINE_BLANK,
            color = SemanticColor.SECONDARY
        )
        val block = Button(
            text = "Block",
            icon = GoogleMaterialDesignIcons.BLOCK,
            color = SemanticColor.DANGER
        )
        val inProgress = Button(
            text = "In Progress",
            icon = GoogleMaterialDesignIcons.TRACK_CHANGES,
            color = SemanticColor.PRIMARY
        )
        val done = Button(
            text = "Done",
            icon = GoogleMaterialDesignIcons.CHECK,
            color = SemanticColor.SUCCESS
        )
    }


    // ---------------------------------------------------------------------------------------------
    // View Drawing
    // ---------------------------------------------------------------------------------------------

    init {
        model["homeLink"] = homeLink
        model["projectLink"] = projectLink
        model["saveButton"] = saveButton
        model["toggleFunction"] = ::toggleEdit
        model["actions"] = actions
        model["deleteButton"] = deleteButton
    }


    private fun onBeforeRendering() {

        issue = model["issue"] as Issue?

        if (edit && issueEditId != null && issueEditId != issue?.id) {
            cancelEdit()
        }

        containerPanel.layout = MigLayout(
            "fill, insets 0",
            "[][grow, fill]50[][${if (!edit) "grow, fill" else ""}]",
            "0[fill, top]0"
        )

        if (!edit) {
            header()
            body()
        } else {
            editForm()
        }
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

            // Navigation Breadcrumbs
            containerPanel.add(breadCrumb, "align left, h 30!, span 3")
            containerPanel.add(editButton, "align right, h 25!, w 70!,  wrap")

            // Title
            containerPanel.add(title, "align left, wrap")
            containerPanel.add(
                subTitle("Details"), "align left, span 2"
            )
            containerPanel.add(
                subTitle("People"),
                "w 75!, wrap"
            )
            // Details part
            containerPanel.add(statusLabel, "w 75!, left")
            containerPanel.add(status, "align left")
            // People Part
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
        issue?.let { it ->

            val descriptionTitle = subTitle("Description")
            val description =
                Typography(
                    text = it.description,
                    color = SemanticColor.DARK.brighter()
                )

            val actionsTitle = subTitle("Actions")
            val actionsSubtitle = Typography(text = "Click to change Issue status")
            val actionsPanel = JPanel(FlowLayout(FlowLayout.LEFT, 25, 0)).also { p -> p.isOpaque = false }

            listOf(actions.toDo, actions.block, actions.inProgress, actions.done)
                .filterNot { button -> button.text.uppercase() in it.status.name.replace("_", " ").uppercase() }
                .forEach { button ->
                    actionsPanel.add(button)
                }

            val dangerZoneTitle =
                Title(text = "Danger Zone", level = TitleLevel.H4, color = SemanticColor.DANGER)
            val warning = Typography(
                text = "Deleting an issue is permanent and cannot be undone.",
            )

            containerPanel.add(TransparentPanel(), "h 75!,growx, span")
            containerPanel.add(descriptionTitle, "align left, wrap")
            containerPanel.add(description, "align left, span, wrap")
            containerPanel.add(TransparentPanel(), "h 50!,growx, span")
            containerPanel.add(actionsTitle, "align left, growx, span")
            containerPanel.add(actionsSubtitle, "align left, growx, span")
            containerPanel.add(TransparentPanel(), "align left, h 15!,span")
            containerPanel.add(actionsPanel, "growx, span")
            containerPanel.add(dangerZoneTitle, "align left, growx, span")
            containerPanel.add(warning, "align left, h 30!, span")
            containerPanel.add(deleteButton, "align center, h 25!, w 100!, span")

        }

    }


    private fun editForm() {

        issueEditId = issue?.id

        val issueForm = IssueForm(issue)

        model["issueForm"] = issueForm

        issue?.let {

            val editText = Title(text = "Edit Mode", color = SemanticColor.PRIMARY, level = TitleLevel.H2)

            // Navigation
            containerPanel.add(editText, "align left, growx, h 30!, span 2")
            containerPanel.add(cancelButton, "align right, h 25!")
            containerPanel.add(saveButton, "align right, h 25!, span")

            // Issue Title
            containerPanel.add(issueForm.title, "align left, wrap")
            containerPanel.add(TransparentPanel(), "h 35!, span, wrap")
            containerPanel.add(
                subTitle("Details"), "align left, span 2"
            )
            containerPanel.add(
                subTitle("People"),
                "w 75!, wrap"
            )
            containerPanel.add(
                Typography(
                    text = "Entering real story points changes status to DONE.",
                    color = SemanticColor.SECONDARY.brighter(),
                    style = Font.ITALIC,
                    size = 10f
                ),
                "span 3, wrap"
            )

            // Details part
            containerPanel.add(issueForm.estimate, "w 75!, span 2")
            containerPanel.add(issueForm.assignee, " w 300!, span")
            containerPanel.add(issueForm.real, "w 75!, span")


            // Description
            containerPanel.add(TransparentPanel(), "h 75!,growx, span, wrap")
            containerPanel.add(subTitle("More Information"), "align left, span, wrap")
            containerPanel.add(issueForm.description.scroll, "align left, growx, span")
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Internal Methods
    // ---------------------------------------------------------------------------------------------

    private fun toggleEdit(e: ActionEvent) {
        cancelEdit()
        repaint()
    }

    private fun cancelEdit() {
        edit = !edit
        issueEditId = null
    }

    private fun subTitle(text: String): Title {
        return Title(
            text = text,
            level = TitleLevel.H4,
            color = SemanticColor.SECONDARY
        )
    }
}