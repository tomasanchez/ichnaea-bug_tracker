package org.ichnaea.controller

import org.ichnaea.core.mvc.controller.UIController
import org.ichnaea.core.ui.button.Button
import org.ichnaea.core.ui.form.TextArea
import org.ichnaea.core.ui.form.TextField
import org.ichnaea.core.ui.semantic.Notification
import org.ichnaea.model.Project
import org.ichnaea.service.ProjectService
import org.slf4j.Logger
import java.awt.event.ActionEvent

@UIController
class NewProjectController : SideViewController() {

    private val projectService = ProjectService()
    private lateinit var nameInput: TextField
    private lateinit var descriptionInput: TextArea
    private lateinit var codeInput: TextField

    companion object {
        private val LOGGER: Logger = org.slf4j.LoggerFactory.getLogger(NewProjectController::class.java)
    }

    // -------------------------------------------------------------
    // Lifecycle Methods
    // -------------------------------------------------------------

    override fun onInit() {
        (byId("cancelButton") as Button).onClick(this::onCancel)
        (byId("createButton") as Button).onClick(this::onAddProject)

        nameInput = byId("nameField") as TextField
        descriptionInput = byId("descriptionArea") as TextArea
        codeInput = byId("codeField") as TextField
    }

    override fun onBeforeRendering() {
    }

    override fun onAfterRendering() {
        updateNavSelection(HOME_NAV)
    }

    // -------------------------------------------------------------
    // Event Handlers
    // -------------------------------------------------------------

    private fun onAddProject(action: ActionEvent) {
        val name = nameInput.text

        val hasError = validateEmpty(name, nameInput)

        if (hasError) {
            popNotification("Please fill out all fields", Notification.Type.ERROR)
            return
        }


        val description = descriptionInput.text
        val code = codeInput.text

        if (code.isBlank()) {
            popNotification("Code will be auto-generated", Notification.Type.WARNING)
        }

        try {
            projectService.save(Project(name = name, description = description, code = code))
            popNotification("Project created successfully", type = Notification.Type.SUCCESS)
        } catch (e: Exception) {
            LOGGER.error("Error creating project", e)
            popNotification(e.message ?: "Couldn't save project. Try again later.", type = Notification.Type.ERROR)
            return
        }

        clearFields()
    }

    private fun onCancel(action: ActionEvent) {
        clearFields()
        navTo("Projects")
    }

    // -------------------------------------------------------------
    // Internal Methods
    // -------------------------------------------------------------

    /**
     * Clear all input fields
     *
     */
    private fun clearFields() {
        nameInput.text = ""
        descriptionInput.text = ""
        codeInput.text = ""

        nameInput.setError(false)
        descriptionInput.setError(false)
        codeInput.setError(false)
    }

}