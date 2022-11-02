package org.ichnaea.controller

import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons
import org.ichnaea.core.exception.EntityNotFoundException
import org.ichnaea.core.mvc.controller.UIController
import org.ichnaea.core.ui.button.Button
import org.ichnaea.core.ui.button.IconButton
import org.ichnaea.core.ui.data.Table
import org.ichnaea.core.ui.form.TextField
import org.ichnaea.core.ui.semantic.Notification
import org.ichnaea.core.ui.semantic.SemanticColor
import org.ichnaea.core.ui.text.Link
import org.ichnaea.core.ui.text.Title
import org.ichnaea.form.IssueForm
import org.ichnaea.model.Issue
import org.ichnaea.model.IssueStatus
import org.ichnaea.model.Project
import org.ichnaea.model.User
import org.ichnaea.service.IssueService
import org.ichnaea.service.ProjectService
import org.ichnaea.service.exceptions.IllegalMemberException
import org.ichnaea.view.BaseView
import org.slf4j.Logger
import java.awt.Dimension
import java.awt.event.ActionEvent
import javax.swing.JPanel

@UIController
class ProjectDetailsController : SideViewController() {

    private val projectService = ProjectService()
    private val issueService = IssueService()

    private lateinit var project: Project

    private var members: List<User> = listOf()
    private var issues: List<Issue> = listOf()


    companion object {
        private val LOGGER: Logger = org.slf4j.LoggerFactory.getLogger(ProjectDetailsController::class.java)
    }

    override fun onPathChange(id: Long) {
        LOGGER.info("Details for Project[id=$id]")

        if (this.pathId != id) {
            LOGGER.warn("Project has changed from ${this.pathId} to $id")
            this.pathId = id
            projectService.findById(id).ifPresent {
                project = it
                members = projectService.findMembers(id)
                issues = issueService.findByProject(id)
                repaint()
            }
            (this.view as BaseView).addToModel("project", project)
        }

    }

    // -------------------------------------------------------------
    // Lifecycle Methods
    // -------------------------------------------------------------

    override fun onInit() {
        byId("addMemberButton")?.let {
            it as Button
            it.onClick(::onAddMember)
        }

        this.view.model["issueForm"]?.let {
            it as IssueForm
            it.submit.onClick(::onReportIssue)
        }

        byId("homeLink")?.let {
            it as Link
            it.onClick = { navTo("projects") }
        }
    }

    override fun onBeforeRendering() {
        hideComponents()
    }

    override fun onAfterRendering() {
        updateNavSelection(HOME_NAV)
        updateIssueTable()
        updateMembersTable()
    }

    // -------------------------------------------------------------
    // Event Handler
    // -------------------------------------------------------------

    /**
     * Event handler for delete member button in table
     *
     * @param userId to delete
     */
    private fun onMemberDelete(userId: Long) {

        if (!isUserAdmin()) {
            popNotification("You are not allowed to delete members", Notification.Type.ERROR)
            return
        }


        members.find { it.id == userId }?.let {

            try {
                projectService.removeMember(project.id, it.id)
                popNotification("${it.userName} removed", Notification.Type.SUCCESS)
                members = projectService.findMembers(project.id)
                updateMembersTable()
            } catch (e: Exception) {
                popNotification("Member could not be removed", Notification.Type.ERROR)
            }

        }

    }

    /**
     * Event handler for when the add member buttons is pressed.
     *
     * @param e
     */

    private fun onAddMember(e: ActionEvent) {

        val idInput = byId("userIdInput") as TextField

        val isEmpty = validateEmpty(idInput.text, idInput)

        if (isEmpty) {
            popNotification("ID must not be empty", Notification.Type.ERROR)
            return
        }

        val userName: String = idInput.text

        idInput.text = ""
        idInput.setError(true)

        val user =
            try {
                projectService.addMember(project.id, userName)
            } catch (enf: EntityNotFoundException) {
                LOGGER.error("User not found")
                popNotification("User Does not Exists", Notification.Type.ERROR)
                return
            } catch (ime: IllegalMemberException) {
                popNotification("${ime.message}", Notification.Type.ERROR)
                return
            }

        members = projectService.findMembers(project.id)
        updateMembersTable()

        clearTextField(idInput)
        popNotification("${user.userName} was added", Notification.Type.SUCCESS)
    }


    private fun onReportIssue(e: ActionEvent) {

        val issueForm = view.model["issueForm"] as IssueForm


        val isEmpty = validateEmpty(issueForm.title.text, issueForm.title) || validateEmpty(
            issueForm.estimate.text,
            issueForm.estimate
        )


        if (isEmpty) {
            popNotification("Title and Story Points must not be empty", Notification.Type.ERROR)
            return
        }


        val points = try {
            issueForm.estimate.text.toInt()
        } catch (e: NumberFormatException) {
            popNotification("Story Points must be a number", Notification.Type.ERROR)
            issueForm.estimate.setError(true)
            return
        }

        val issue =
            try {

                issueService.reportIssue(
                    project.id,
                    issueForm.title.text.trim(),
                    issueForm.description.text.trim(),
                    points,
                    issueForm.assignee.text.trim(),
                )
            } catch (enf: IllegalMemberException) {
                LOGGER.error("User not found")
                popNotification(enf.message ?: "Invalid Username", Notification.Type.ERROR)
                issueForm.assignee.setError(true)
                return
            }

        popNotification(
            message = "Issue ${issue.id} was reported",
            type = Notification.Type.SUCCESS
        )

        issueForm.clear()

        issues = issueService.findByProject(project.id)
        updateIssueTable()
    }

    // -------------------------------------------------------------
    // Internal Methods
    // -------------------------------------------------------------

    private fun updateIssueTable() {
        val issueTable = byId("issuesTable") as Table
        val title = byId("issuesTitle") as Title
        issueTable.clear()

        if (issues.isEmpty()) {
            title.text = "No Issues"
            return
        }

        title.text = "Issues (${issues.size})"

        issues.forEach {

            val withStatusRow = it.toTableRow().toMutableList()


            val statusButton =
                Button(
                    text = it.getStatusLabel(),
                    color = when (it.status) {
                        IssueStatus.TO_DO -> SemanticColor.SECONDARY
                        IssueStatus.BLOCKED -> SemanticColor.DANGER
                        IssueStatus.IN_PROGRESS -> SemanticColor.PRIMARY
                        IssueStatus.DONE -> SemanticColor.SUCCESS
                    }
                ).also { button ->
                    button.minimumSize = Dimension(120, 30)
                }

            val containerPanel = JPanel().also { panel ->
                panel.add(statusButton)
                panel.isOpaque = false
            }

            withStatusRow.add(containerPanel)

            issueTable.addRow(withStatusRow.toTypedArray())
        }

        issueTable.onRowClick { navTo("issueDetails", it as Long) }

    }

    private fun updateMembersTable() {
        val table = byId("membersTable") as Table
        val title = byId("membersTitle") as Title
        table.clear()

        val noAdminMembers = members.filter { it.roleId == 1L }

        if (noAdminMembers.isNotEmpty()) {
            title.text = "Members (${noAdminMembers.size})"

            noAdminMembers.forEach { member ->
                val withActionsRow =
                    member
                        .toTableRow()
                        .asList()
                        .toMutableList()

                withActionsRow.add(
                    IconButton(
                        code = GoogleMaterialDesignIcons.DELETE,
                        color = SemanticColor.DANGER,
                        size = 20f,
                    ).also {
                        it.isOpaque = false
                        it.isBorderPainted = false
                    }
                )

                table.addRow(withActionsRow.toTypedArray() as Array<Any>)
            }

            table.onCellClick(2) {
                onMemberDelete(it as Long)
            }

        } else {
            title.text = "No members found"
        }

    }

    // -------------------------------------------------------------
    // Hide Admin Components
    // -------------------------------------------------------------


    private fun hideComponents() {
        val isAdmin = isUserAdmin()
        byId("userForm")?.let {
            it.isVisible = isAdmin
        }
    }

}