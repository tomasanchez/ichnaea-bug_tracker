package org.ichnaea.dao

import org.ichnaea.model.Role
import org.ichnaea.model.User
import java.util.*

interface RoleDAO : PersistentEntityDAO<Role>

interface UserDAO : PersistentEntityDAO<User> {
    fun findByUsername(username: String): Optional<User>
}