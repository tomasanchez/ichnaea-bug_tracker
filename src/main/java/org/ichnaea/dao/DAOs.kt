package org.ichnaea.dao

import org.ichnaea.model.Project
import org.ichnaea.model.Role
import org.ichnaea.model.User
import java.util.*

interface RoleDAO : PersistentEntityDAO<Role>

interface UserDAO : PersistentEntityDAO<User> {
    fun findByUsername(username: String): Optional<User>
}

interface ProjectDAO : PersistentEntityDAO<Project> {

    /**
     * Find all project members
     *
     * @param id of the project
     * @return a list of user ids
     */
    fun findMembers(id: Long): List<Long>

    /**
     * Add user to project
     *
     * @param userId the user unique identifier
     * @param projectId the project unique identifier
     */
    fun addUser(userId: Long, projectId: Long)

    /**
     * Removes a user from a project
     *
     * @param userId the user unique identifier
     * @param projectId the project unique identifier
     */
    fun removeUser(userId: Long, projectId: Long)

    /**
     * Find projects of a user
     *
     * @param userId to be searched
     * @return a list of project in which the user is a member
     */
    fun findProjects(userId: Long): List<Project>

}