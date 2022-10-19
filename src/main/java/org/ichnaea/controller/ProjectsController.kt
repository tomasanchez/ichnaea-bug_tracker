package org.ichnaea.controller

import org.ichnaea.core.mvc.controller.Controller
import org.ichnaea.core.ui.data.Table
import org.ichnaea.service.ProjectService

@Controller
class ProjectsController : SideViewController() {

    private val projectService = ProjectService()

    lateinit var oTable: Table

    override fun onInit() {

        byId("projectsTable")?.let {
            oTable = it as Table
        }

        @Suppress("UNCHECKED_CAST")
        projectService.findAll().forEach {
            oTable.addRow(it.toTableRow() as Array<Any>)
        }

    }


}