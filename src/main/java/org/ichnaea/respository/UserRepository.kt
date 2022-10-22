package org.ichnaea.respository

import org.ichnaea.dao.PersistentEntityDAO
import org.ichnaea.model.User

object UserRepository : ListRepository<User>() {

    private lateinit var userDAO: PersistentEntityDAO<User>


    fun findByUsername(username: String): User? {
        return findAll().find { it.userName == username }
    }

}