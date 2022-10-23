package org.ichnaea.controller

import org.ichnaea.core.mvc.controller.UIController
import org.ichnaea.core.ui.button.Button
import org.ichnaea.core.ui.data.Table
import org.ichnaea.core.ui.text.Title
import org.ichnaea.service.ProjectService

@UIController
class ProjectsController : SideViewController() {

    private val projectService = ProjectService()

    lateinit var oTable: Table


    // -------------------------------------------------------------
    // Lifecycle Methods
    // -------------------------------------------------------------

    override fun onInit() {

        byId("viewTitle")?.let {
            val title = it as Title
            title.text = "No data found"
        }

        byId("projectsTable")?.let {
            oTable = it as Table
        }

        byId("newProjectButton")?.let {
            (it as Button).onClick {
                navTo("NewProject")
            }
        }

        oTable.onRowClick { navTo("projectDetails", it as Long) }

    }

    override fun onBeforeRendering() {
        updateTable()

        byId("newProjectButton")?.let {
            it.isVisible = isUserAdmin()
        }
    }

    override fun onAfterRendering() {
        updateNavSelection(HOME_NAV)
    }

    // -------------------------------------------------------------
    // Internal methods
    // -------------------------------------------------------------


    private fun updateTable() {
        oTable.clear()

        val projects = projectService.findByUser(user!!)

        @Suppress("UNCHECKED_CAST")
        projects.forEach { oTable.addRow(it.toTableRow() as Array<Any>) }

        byId("viewTitle")?.let {
            val title = it as Title
            title.text = if (projects.isNotEmpty()) "Projects (${projects.size})" else "No Projects"
        }
    }

}