package org.ichnaea.controller

import org.ichnaea.core.mvc.controller.Controller
import org.ichnaea.model.Project
import org.ichnaea.service.ProjectService
import org.ichnaea.view.BaseView
import org.slf4j.Logger

@Controller
class ProjectDetailsController : SideViewController() {

    private val projectService = ProjectService()

    private lateinit var project: Project

    companion object {
        private val LOGGER: Logger = org.slf4j.LoggerFactory.getLogger(ProjectDetailsController::class.java)
    }

    override fun onPathChange(id: Long) {
        LOGGER.info("Details for Project[id=$id]")

        if (this.pathId != id) {
            LOGGER.warn("PathId has changed from ${this.pathId} to $id")
            this.pathId = id
            projectService.findById(id).ifPresent {
                project = it
                repaint()
            }
            (this.view as BaseView).addToModel("project", project)
        }

    }

    override fun onInit() {
    }

}