package org.ichnaea.controller

import org.ichnaea.core.mvc.controller.UIController
import org.ichnaea.core.ui.text.Link
import org.ichnaea.model.Issue
import org.ichnaea.service.IssueService
import org.ichnaea.service.ProjectService
import org.ichnaea.view.BaseView
import org.slf4j.Logger

@UIController
class IssueDetailsController : SideViewController() {

    private val issueService = IssueService()
    private val projectService = ProjectService()


    private lateinit var issue: Issue

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

        if (this.pathId != id) {

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

    }

    // -------------------------------------------------------------
    // Internal Methods
    // -------------------------------------------------------------

}