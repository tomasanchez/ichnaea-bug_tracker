package org.ichnaea.controller

import org.ichnaea.core.mvc.controller.UIController
import org.ichnaea.core.ui.button.Button
import org.ichnaea.core.ui.semantic.Notification
import org.ichnaea.core.ui.text.Link
import org.ichnaea.form.IssueForm
import org.ichnaea.model.Issue
import org.ichnaea.model.IssueStatus
import org.ichnaea.service.IssueService
import org.ichnaea.service.ProjectService
import org.ichnaea.service.exceptions.IllegalMemberException
import org.ichnaea.view.BaseView
import org.ichnaea.view.IssueDetailsView
import org.slf4j.Logger
import java.awt.event.ActionEvent


@UIController
class IssueDetailsController : SideViewController() {

    private val issueService = IssueService()
    private val projectService = ProjectService()


    private lateinit var issue: Issue
    private lateinit var issueForm: IssueForm
    private lateinit var actions: IssueDetailsView.IssueActions


    companion object {
        private val LOGGER: Logger = org.slf4j.LoggerFactory.getLogger(IssueDetailsController::class.java)
    }

    // -------------------------------------------------------------
    // Lifecycle Methods
    // -------------------------------------------------------------

    override fun onInit() {
        byId("homeLink")?.let {
            it as Link
            it.onClick = { navTo("projects") }
        }

        byId("saveButton")?.let {
            it as Button
            it.onClick(::onSave)
        }

        byId("deleteButton")?.let {
            it as Button
            it.onClick(::onDelete)
        }


        view.model["actions"]?.let {
            it as IssueDetailsView.IssueActions
            actions = it

            actions.toDo.onClick { e -> onStatusChange(e, IssueStatus.TO_DO) }
            actions.block.onClick { e -> onStatusChange(e, IssueStatus.BLOCKED) }
            actions.inProgress.onClick { e -> onStatusChange(e, IssueStatus.IN_PROGRESS) }
            actions.done.onClick { e -> onStatusChange(e, IssueStatus.DONE) }

        }
    }


    override fun onBeforeRendering() {
    }

    override fun onAfterRendering() {
        updateNavSelection(HOME_NAV)
    }

    // -------------------------------------------------------------
    // Event Handler
    // -------------------------------------------------------------

    override fun onPathChange(id: Long) {

        LOGGER.info("Details for Issue[id=$id]")

        LOGGER.warn("Issue has changed from ${this.pathId} to $id")

        this.pathId = id

        issueService.findById(id).ifPresent {
            issue = it

            projectService
                .findById(issue.projectId)
                .ifPresent { project ->
                    byId("projectLink")
                        ?.let { link ->
                            link as Link
                            link.text = project.code
                            link.onClick = { navTo("projectDetails", project.id) }
                        }
                }

            repaint()
        }


        (this.view as BaseView).addToModel("issue", issue)
    }

    private fun onSave(event: ActionEvent) {
        @Suppress("UNCHECKED_CAST")
        val toggle = view.model["toggleFunction"] as ((ActionEvent) -> Unit)?

        val hasError = formHasError()

        if (hasError) {
            popNotification(message = "Title and Estimate must not be blank", type = Notification.Type.ERROR)
            return
        }

        issueForm = view.model["issueForm"] as IssueForm

        val estimate: Int
        val real: Int?

        try {
            estimate = issueForm.estimate.text.toInt()
            real = if (issueForm.real.text.isBlank()) null else issueForm.real.text.toInt()
        } catch (e: NumberFormatException) {
            popNotification(message = "Estimate and Real must be a number", type = Notification.Type.ERROR)
            issueForm.estimate.setError(true)
            return
        }

        try {
            issueService.update(
                issueID = issue.id,
                title = issueForm.title.text,
                description = issueForm.description.text,
                estimate = estimate,
                real = real,
                username = issueForm.assignee.text
            )
        } catch (ilme: IllegalMemberException) {
            popNotification(message = ilme.message ?: "User is not allowed", type = Notification.Type.ERROR)
            issueForm.assignee.setError(true)
            return
        } catch (e: Exception) {
            popNotification(message = e.message ?: "Error while saving", type = Notification.Type.ERROR)
            return
        }


        onPathChange(issue.id)
        popNotification(message = "Issue updated", type = Notification.Type.SUCCESS)
        toggle?.let { it(event) }
    }


    private fun onStatusChange(e: ActionEvent, status: IssueStatus) {

        try {

            when (status) {

                IssueStatus.DONE -> {
                    popNotification(message = "Enter the time spent", type = Notification.Type.INFO)
                    view.model["toggleFunction"]?.let {
                        @Suppress("UNCHECKED_CAST")
                        it as (ActionEvent) -> Unit
                        it(e)
                    }
                }

                else -> {
                    issueService.updateStatus(issue.id, status)
                    popNotification(message = "Status updated", type = Notification.Type.SUCCESS)
                    navTo("issueDetails", issue.id)
                }
            }


        } catch (e: Exception) {
            popNotification(message = e.message ?: "Could not change status", type = Notification.Type.ERROR)
            return
        }

    }

    private fun onDelete(e: ActionEvent) {
        try {
            issueService.delete(issue.id)
            popNotification(message = "Issue deleted", type = Notification.Type.WARNING)
            navTo("projectDetails", issue.projectId)
        } catch (e: Exception) {
            popNotification(message = e.message ?: "Could not delete issue", type = Notification.Type.ERROR)
            return
        }
    }

    // -------------------------------------------------------------
    // Internal Methods
    // -------------------------------------------------------------
    private fun formHasError(): Boolean {
        issueForm = view.model["issueForm"] as IssueForm

        return validateEmpty(issueForm.title.text, issueForm.title)
                || validateEmpty(issueForm.estimate.text, issueForm.estimate)
    }


}