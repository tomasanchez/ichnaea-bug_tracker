package org.ichnaea.controller

import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons
import org.ichnaea.core.mvc.controller.UIController
import org.ichnaea.core.ui.button.Button
import org.ichnaea.core.ui.button.IconButton
import org.ichnaea.core.ui.semantic.Notification
import org.ichnaea.core.ui.semantic.SemanticColor
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
        byId("addMemberButton")?.let {
            it as Button
            it.onClick {
                LOGGER.info("Add Member Button Clicked")
                popNotification("Member Added", Notification.Type.SUCCESS)
            }
        }
    }

    override fun onBeforeRendering() {
        isUserAdmin()
    }

    override fun onAfterRendering() {
        updateNavSelection(HOME_NAV)
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
                    )
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


}