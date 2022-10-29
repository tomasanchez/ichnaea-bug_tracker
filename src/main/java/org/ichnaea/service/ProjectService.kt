package org.ichnaea.service

import org.ichnaea.core.exception.EntityNotFoundException
import org.ichnaea.core.exception.PersistenceException
import org.ichnaea.model.Project
import org.ichnaea.model.User
import org.ichnaea.respository.ProjectRepository
import org.ichnaea.respository.UserRepository
import org.ichnaea.respository.dao.ProjectDAORepository
import org.ichnaea.respository.dao.UserDAORepository
import org.ichnaea.service.exceptions.IllegalMemberException

class ProjectService(
    projectRepository: ProjectRepository = ProjectDAORepository(),
    private val userRepository: UserRepository = UserDAORepository(),
) : TransactionalService<Project>(projectRepository) {

    /**
     * Finds all projects in which a user is a member. If user is admin shows all projects.
     *
     * @param user member of the projects
     * @return a list of projects
     */
    fun findByUser(user: User): List<Project> {
        return if (isAdmin(user)) {
            repository.findAll()
        } else {
            (repository as ProjectRepository).findProjects(user.id)
        }
    }

    /**
     * Finds members of a project
     *
     * @param id of a project
     * @return a list of user members
     */
    fun findMembers(id: Long): List<User> {
        return (repository as ProjectRepository).findMembers(id)
    }

    fun removeMember(projectId: Long, userId: Long) {
        (repository as ProjectRepository).removeMember(projectId, userId)
    }

    /**
     * Adds a member to a Project
     *
     * @param projectId id of a project
     * @param userName of an existing user
     * @return the added user
     */
    fun addMember(projectId: Long, userName: String): User {
        val user = userRepository.findByUsername(userName).orElseThrow { EntityNotFoundException("User not found") }

        if (isAdmin(user)) {
            throw IllegalMemberException("Admins can't be added to projects")
        } else try {
            (repository as ProjectRepository).addMember(projectId, user.id)
        } catch (pe: PersistenceException) {
            throw IllegalMemberException("User is already a member of this project")
        }

        return user
    }

    private fun isAdmin(user: User): Boolean {
        return user.role.name.toString().equals("admin", true)
    }

}
