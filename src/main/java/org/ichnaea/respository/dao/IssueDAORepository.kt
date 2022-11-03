package org.ichnaea.respository.dao

import org.ichnaea.dao.IssueDAO
import org.ichnaea.dao.UserDAO
import org.ichnaea.model.Issue
import org.ichnaea.respository.IssueRepository
import java.util.*

class IssueDAORepository : DAORepository<Issue>(), IssueRepository {

    private val issueDAO: IssueDAO = org.ichnaea.dao.sqlite.SQLiteIssueDAO()
    private val userDAO: UserDAO = org.ichnaea.dao.sqlite.SQLiteUserDAO()

    override fun getDAO(): IssueDAO = issueDAO

    override fun findByProject(projectId: Long): List<Issue> {
        val issues = issueDAO.findByProject(projectId)

        val assigneesIds = issues.filter { it.assigneeId != null }.map { it.assigneeId }.toSet().toList()

        val assignees = userDAO.findByIds(assigneesIds)

        val issuesWithUser = issues.map { issue ->
            issue.copy(assignee = assignees.find { it.id == issue.assigneeId }).also { it.id = issue.id }
        }

        return issuesWithUser
    }


    override fun findById(id: Long): Optional<Issue> = super.findById(id).map(::withUser)

    private fun withUser(issue: Issue): Issue {

        issue.assigneeId?.let { id ->
            val assignee = userDAO.findById(id).orElse(null)
            return issue.copy(assignee = assignee).also { it.id = issue.id }
        }

        return issue
    }

    override fun unAssign(issueId: Long) = issueDAO.unAssign(issueId)
}