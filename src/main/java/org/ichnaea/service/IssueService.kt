package org.ichnaea.service

import org.ichnaea.core.exception.EntityNotFoundException
import org.ichnaea.model.Issue
import org.ichnaea.model.IssueStatus
import org.ichnaea.model.RoleName
import org.ichnaea.model.User
import org.ichnaea.respository.IssueRepository
import org.ichnaea.respository.PersistentEntityRepository
import org.ichnaea.respository.ProjectRepository
import org.ichnaea.respository.UserRepository
import org.ichnaea.respository.dao.IssueDAORepository
import org.ichnaea.respository.dao.ProjectDAORepository
import org.ichnaea.respository.dao.UserDAORepository
import org.ichnaea.service.exceptions.IllegalMemberException

class IssueService(
    repository: PersistentEntityRepository<Issue> = IssueDAORepository(),
) : TransactionalService<Issue>(repository) {

    private val projectRepository: ProjectRepository = ProjectDAORepository()
    private val userRepository: UserRepository = UserDAORepository()


    fun findByProject(projectId: Long): List<Issue> {
        return (repository as IssueRepository).findByProject(projectId)
    }


    fun reportIssue(projectId: Long, title: String, description: String, storyPoints: Int, username: String): Issue {

        val user: User? =
            fetchUser(username)

        verifyUser(user, projectId, username)


        val issue = Issue(
            title = title,
            description = description,
            projectId = projectId,
            estimatedPoints = storyPoints,
            assigneeId = user?.id,
        )

        return (repository as IssueRepository).save(issue)
    }

    fun updateStatus(issueId: Long, status: IssueStatus) = (repository as IssueRepository).setStatus(issueId, status)


    fun update(
        issueID: Long,
        title: String,
        description: String,
        estimate: Int,
        real: Int?,
        username: String,
    ): Issue {

        val user: User? =
            fetchUser(username)

        unAssign(issueID)

        val originalIssue =
            findById(issueID)
                .orElseThrow { EntityNotFoundException("Issue[id=$issueID] not found") }

        verifyUser(user, originalIssue.projectId, username)

        val status =
            if (real != null) IssueStatus.DONE
            else originalIssue.status

        val toUpdate = originalIssue.copy(
            title = title,
            description = description,
            estimatedPoints = estimate,
            realPoints = real,
            assigneeId = user?.id,
            status = status,
        ).also {
            it.id = issueID
        }


        return repository.save(toUpdate)
    }

    private fun fetchUser(username: String): User? =
        if (username.isNotBlank())
            userRepository
                .findByUsername(username)
                .orElseThrow { IllegalMemberException("User $username does not exist") }
        else null

    private fun verifyUser(user: User?, projectId: Long, username: String) {
        user
            ?.let {
                if (it.role.name != RoleName.ADMIN)
                    projectRepository
                        .findMembers(projectId)
                        .find { member -> member.id == it.id }
                        ?: throw IllegalMemberException("User $username is not a member of this project")
            }
    }

    private fun unAssign(issueID: Long) {
        (repository as IssueRepository).unAssign(issueID)
    }

}