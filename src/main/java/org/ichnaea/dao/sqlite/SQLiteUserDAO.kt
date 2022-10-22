package org.ichnaea.dao.sqlite

import org.ichnaea.core.exception.PersistenceException
import org.ichnaea.dao.UserDAO
import org.ichnaea.model.User
import java.util.*

class SQLiteUserDAO : SQLiteDAO<User>(), UserDAO {

    override fun entityMapper(resultMap: Map<String, Any>): User = User(
        userName = resultMap["user_name"] as String,
        password = resultMap["password"] as String,
        roleId = (resultMap["role_id"] as Int).toLong(),
    )

    /**
     * Finds a USER by username index
     *
     * @param username the unique index
     * @return optionally a user, otherwise null
     */
    override fun findByUsername(username: String): Optional<User> {

        val sql = "SELECT * FROM ${getTableName()} WHERE user_name = ?"

        var user: User? = null

        try {
            connect()
            val statement = createPreparedStatement(sql)!!
            statement.setString(1, username)
            val resultSet = statement.executeQuery()

            user = resultSetToMapList(resultSet).map(::toEntity).firstOrNull()
        } catch (e: Exception) {
            throw PersistenceException("Error while finding user by username", e)
        } finally {
            disconnect()
        }
        return user?.let { Optional.of(it) } ?: Optional.empty()
    }

}