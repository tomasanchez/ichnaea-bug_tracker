package org.ichnaea.service

import org.ichnaea.model.Project
import org.ichnaea.model.User
import org.ichnaea.respository.ProjectRepository
import org.ichnaea.respository.dao.ProjectDAORepository

class ProjectService(
    projectRepository: ProjectRepository = ProjectDAORepository(),
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

    private fun isAdmin(user: User): Boolean {
        return user.role.name.toString().equals("admin", true)
    }

}
