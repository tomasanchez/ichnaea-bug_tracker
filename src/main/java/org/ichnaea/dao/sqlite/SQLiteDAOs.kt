package org.ichnaea.dao.sqlite

import org.ichnaea.dao.RoleDAO
import org.ichnaea.model.Role
import org.ichnaea.model.RoleName


class SQLiteRoleDAO : SQLiteDAO<Role>(), RoleDAO {

    override fun entityMapper(resultMap: Map<String, Any>): Role = Role(
        name = RoleName.values().first { it.name.equals(resultMap["name"] as String, true) }
    )

}
