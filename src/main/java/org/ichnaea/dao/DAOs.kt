package org.ichnaea.dao

import org.ichnaea.model.Role
import org.ichnaea.model.User

interface RoleDAO : PersistentEntityDAO<Role>

interface UserDAO : PersistentEntityDAO<User>