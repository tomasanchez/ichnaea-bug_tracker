package org.ichnaea.controller

import org.ichnaea.core.mvc.controller.UIController
import org.ichnaea.core.ui.data.Table
import org.ichnaea.model.Issue
import org.ichnaea.model.IssueStatus
import org.ichnaea.model.Project
import org.ichnaea.service.IssueService
import org.ichnaea.service.ProjectService

@UIController
class ProjectReportController : SideViewController() {

    private val projectService = ProjectService()

    private val issueService = IssueService()

    private lateinit var table: Table

    // -------------------------------------------------------------
    // Lifecycle Methods
    // -------------------------------------------------------------

    override fun onInit() {
        table = byId("table") as Table
    }

    override fun onBeforeRendering() {
        fetchProjects()
            .map(::toProjectRow)
            .forEach(table::addRow)
    }

    override fun onAfterRendering() {
    }

    // -------------------------------------------------------------
    // Lifecycle Methods
    // -------------------------------------------------------------

    private fun fetchProjects(): List<Project> = projectService.findAll()

    private fun toProjectRow(project: Project): Array<Any> {
        val issues: List<Issue> =
            issueService
                .findByProject(project.id)

        val issuesByStatus: Map<IssueStatus, Int> =
            issues
                .groupBy { it.status }
                .map { it.key to it.value.size }
                .toMap()

        return arrayOf(
            project.name,
            issuesByStatus[IssueStatus.TO_DO] ?: 0,
            issuesByStatus[IssueStatus.BLOCKED] ?: 0,
            issuesByStatus[IssueStatus.IN_PROGRESS] ?: 0,
            issuesByStatus[IssueStatus.DONE] ?: 0,
            issues.size,
            issues.sumOf { it.estimatedPoints },
            issues.sumOf { it.realPoints ?: 0 },
        )
    }

}