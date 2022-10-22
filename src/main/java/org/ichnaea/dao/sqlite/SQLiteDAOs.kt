package org.ichnaea.dao.sqlite

import org.ichnaea.dao.RoleDAO
import org.ichnaea.dao.UserDAO
import org.ichnaea.model.Role
import org.ichnaea.model.RoleName
import org.ichnaea.model.User


class SQLiteRoleDAO : SQLiteDAO<Role>(), RoleDAO {

    override fun entityMapper(resultMap: Map<String, Any>): Role = Role(
        name = RoleName.values().first { it.name.equals(resultMap["name"] as String, true) }
    )

}


class SQLiteUserDAO : SQLiteDAO<User>(), UserDAO {

    override fun entityMapper(resultMap: Map<String, Any>): User = User(
        userName = resultMap["user_name"] as String,
        password = resultMap["password"] as String,
        roleId = resultMap["role_id"] as Long,
    )

}