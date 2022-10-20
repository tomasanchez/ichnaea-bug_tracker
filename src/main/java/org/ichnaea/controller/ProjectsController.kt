package org.ichnaea.controller

import org.ichnaea.core.mvc.controller.UIController
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
            title.text = "Projects (${projectService.findAll().size})"
        }

        byId("projectsTable")?.let {
            oTable = it as Table
        }

        oTable.onRowClick { navTo("projectDetails", it as Long) }

        @Suppress("UNCHECKED_CAST")
        projectService.findAll().forEach {
            oTable.addRow(it.toTableRow() as Array<Any>)
        }

    }

    override fun onBeforeRendering() {
    }

    override fun onAfterRendering() {
    }


}