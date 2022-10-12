package org.ichnaea.respository

import org.ichnaea.model.User

class UserRepository : CrudRepository<User>() {

    fun findByUsername(username: String): User? {
        return findAll().find { it.userName == username }
    }

}