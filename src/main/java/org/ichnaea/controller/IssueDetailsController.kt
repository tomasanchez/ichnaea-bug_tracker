package org.ichnaea.controller

import org.ichnaea.core.mvc.controller.UIController
import org.ichnaea.core.ui.button.Button
import org.ichnaea.core.ui.semantic.Notification
import org.ichnaea.core.ui.text.Link
import org.ichnaea.form.IssueForm
import org.ichnaea.model.Issue
import org.ichnaea.service.IssueService
import org.ichnaea.service.ProjectService
import org.ichnaea.service.exceptions.IllegalMemberException
import org.ichnaea.view.BaseView
import org.slf4j.Logger
import java.awt.event.ActionEvent


@UIController
class IssueDetailsController : SideViewController() {

    private val issueService = IssueService()
    private val projectService = ProjectService()


    private lateinit var issue: Issue
    lateinit var issueForm: IssueForm


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


    // -------------------------------------------------------------
    // Internal Methods
    // -------------------------------------------------------------
    private fun formHasError(): Boolean {
        issueForm = view.model["issueForm"] as IssueForm

        return validateEmpty(issueForm.title.text, issueForm.title)
                || validateEmpty(issueForm.estimate.text, issueForm.estimate)
    }

}