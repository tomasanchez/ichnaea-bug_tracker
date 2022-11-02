package org.ichnaea.dao.sqlite

import org.ichnaea.core.exception.PersistenceException
import org.ichnaea.dao.ProjectDAO
import org.ichnaea.model.Project

class SQLiteProjectDAO : SQLiteDAO<Project>(), ProjectDAO {

    companion object {
        const val PROJECT_MEMBERS_TABLE = "PROJECT_USER"
        const val ISSUE_TABLE = "ISSUE"
    }

    override fun entityMapper(resultMap: Map<String, Any>): Project {
        return Project(
            name = resultMap["name"] as String,
            description = resultMap["description"] as String,
            code = resultMap["code"] as String,
        )
    }

    override fun findMembers(id: Long): List<Long> {
        return findRelatedFor(id, PROJECT_MEMBERS_TABLE).map { it["user_id"]!! }
    }

    override fun addUser(userId: Long, projectId: Long) {
        val sql = "INSERT INTO $PROJECT_MEMBERS_TABLE (user_id, project_id) VALUES (?, ?)"
        try {
            connect()
            val statement = createPreparedStatement(sql)!!
            statement.setInt(1, userId.toInt())
            statement.setInt(2, projectId.toInt())

            val rowCount = statement.executeUpdate()

            if (rowCount != 1) {
                throw PersistenceException("No rows affected while adding user to project")
            }

        } catch (e: Exception) {
            throw PersistenceException("Error while adding user to project", e)
        } finally {
            disconnect()
        }
    }

    override fun removeUser(userId: Long, projectId: Long) {
        val sql = "DELETE FROM $PROJECT_MEMBERS_TABLE WHERE user_id = ? AND project_id = ?"
        val sql2 = "UPDATE $ISSUE_TABLE SET user_id = NULL WHERE user_id = ? AND project_id = ?"

        try {
            connect()
            val statement = createPreparedStatement(sql)!!

            statement.setInt(1, userId.toInt())
            statement.setInt(2, projectId.toInt())

            val rowCount = statement.executeUpdate()

            if (rowCount != 1) {
                throw PersistenceException("No rows affected while removing user from project")
            }

            val statement2 = createPreparedStatement(sql2)!!
            statement2.setInt(1, userId.toInt())
            statement2.setInt(2, projectId.toInt())

            statement2.executeUpdate()
        } catch (e: Exception) {
            throw PersistenceException("Error while removing user from project", e)
        } finally {
            disconnect()
        }
    }

    override fun findProjects(userId: Long): List<Project> {
        val sql =
            "SELECT * FROM ${getTableName()}" +
                    " WHERE id IN (SELECT project_id FROM $PROJECT_MEMBERS_TABLE WHERE user_id = ?)"

        try {
            connect()
            val statement = createPreparedStatement(sql)!!
            statement.setInt(1, userId.toInt())
            val resultSet = statement.executeQuery()

            return resultSetToMapList(resultSet).map(::toEntity)
        } catch (e: Exception) {
            throw PersistenceException("Error while finding projects of user", e)
        } finally {
            disconnect()
        }
    }

}