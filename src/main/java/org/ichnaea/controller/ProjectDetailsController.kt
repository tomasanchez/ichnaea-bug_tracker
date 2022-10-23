package org.ichnaea.controller

import org.ichnaea.core.mvc.controller.UIController
import org.ichnaea.model.Project
import org.ichnaea.model.User
import org.ichnaea.service.ProjectService
import org.ichnaea.view.BaseView
import org.slf4j.Logger

@UIController
class ProjectDetailsController : SideViewController() {

    private val projectService = ProjectService()

    private lateinit var project: Project

    private var members: List<User> = listOf()

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
                repaint()
            }
            (this.view as BaseView).addToModel("project", project)
        }

    }

    // -------------------------------------------------------------
    // Lifecycle Methods
    // -------------------------------------------------------------

    override fun onInit() {
    }

    override fun onBeforeRendering() {
    }

    override fun onAfterRendering() {
        updateNavSelection(HOME_NAV)
        updateMembersTable()
    }

    // -------------------------------------------------------------
    // Internal Methods
    // -------------------------------------------------------------

    private fun updateMembersTable() {
        val table = byId("membersTable") as org.ichnaea.core.ui.data.Table
        val title = byId("membersTitle") as org.ichnaea.core.ui.text.Title
        table.clear()

        val noAdminMembers = members.filter { it.roleId == 1L }

        if (noAdminMembers.isNotEmpty()) {
            title.text = "Members (${noAdminMembers.size})"
            @Suppress("UNCHECKED_CAST")
            noAdminMembers.forEach {
                table.addRow(it.toTableRow() as Array<Any>)
            }
        } else {
            title.text = "No members found"
        }

    }

}