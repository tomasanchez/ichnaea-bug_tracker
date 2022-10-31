package org.ichnaea.service

import org.ichnaea.model.Issue
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
            if (username.isNotBlank())
                userRepository
                    .findByUsername(username)
                    .orElseThrow { IllegalMemberException("User $username does not exist") }
            else null

        user?.let {
            if (it.role.name != RoleName.ADMIN) {
                projectRepository.findMembers(projectId).find { member -> member.id == user.id }
                    ?: throw IllegalMemberException("User $username is not a member of this project")
            }
        }


        val issue = Issue(
            title = title,
            description = description,
            projectId = projectId,
            estimatedPoints = storyPoints,
            assigneeId = user?.id,
        )

        return (repository as IssueRepository).save(issue)
    }

}