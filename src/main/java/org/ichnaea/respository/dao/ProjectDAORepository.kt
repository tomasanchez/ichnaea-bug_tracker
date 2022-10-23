package org.ichnaea.respository.dao

import org.ichnaea.dao.PersistentEntityDAO
import org.ichnaea.dao.ProjectDAO
import org.ichnaea.dao.UserDAO
import org.ichnaea.dao.sqlite.SQLiteProjectDAO
import org.ichnaea.dao.sqlite.SQLiteUserDAO
import org.ichnaea.model.Project
import org.ichnaea.model.User
import org.ichnaea.respository.ProjectRepository

class ProjectDAORepository : DAORepository<Project>(), ProjectRepository {

    private val projectDAO: ProjectDAO = SQLiteProjectDAO()
    private val userDAO: UserDAO = SQLiteUserDAO()

    override fun getDAO(): PersistentEntityDAO<Project> {
        return projectDAO
    }

    override fun findMembers(id: Long): MutableList<User> {
        return userDAO.findByIds(projectDAO.findMembers(id))
    }

    override fun addMember(id: Long, userId: Long) {
        projectDAO.addUser(userId, id)
    }

    override fun removeMember(id: Long, userId: Long) {
        projectDAO.removeUser(userId, id)
    }

    override fun findProjects(id: Long): List<Project> {
        return projectDAO.findProjects(id)
    }

}
