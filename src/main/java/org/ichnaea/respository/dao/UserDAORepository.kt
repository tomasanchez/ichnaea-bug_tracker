package org.ichnaea.respository.dao

import org.ichnaea.core.exception.PersistenceException
import org.ichnaea.dao.PersistentEntityDAO
import org.ichnaea.dao.RoleDAO
import org.ichnaea.dao.UserDAO
import org.ichnaea.dao.sqlite.SQLiteRoleDAO
import org.ichnaea.dao.sqlite.SQLiteUserDAO
import org.ichnaea.model.User
import org.ichnaea.respository.UserRepository
import java.util.*

class UserDAORepository : DAORepository<User>(), UserRepository {

    private val userDAO: UserDAO = SQLiteUserDAO()
    private val roleDAO: RoleDAO = SQLiteRoleDAO()

    override fun getDAO(): PersistentEntityDAO<User> {
        return userDAO
    }

    override fun findByUsername(username: String): Optional<User> {
        return userDAO.findByUsername(username)
            .map { it.copy(role = roleDAO.findById(it.roleId).orElseThrow { PersistenceException("Role not found") }) }
    }

}