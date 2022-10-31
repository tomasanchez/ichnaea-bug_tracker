package org.ichnaea.dao.sqlite

import org.ichnaea.core.exception.PersistenceException
import org.ichnaea.dao.IssueDAO
import org.ichnaea.model.Issue
import org.ichnaea.model.IssueStatus

class SQLiteIssueDAO : SQLiteDAO<Issue>(), IssueDAO {

    override fun entityMapper(resultMap: Map<String, Any>): Issue {
        return Issue(
            assigneeId = (resultMap["user_id"] as Int?)?.toLong(),
            projectId = (resultMap["project_id"] as Int).toLong(),
            title = resultMap["title"] as String,
            description = resultMap["description"] as String,
            status = IssueStatus.valueOf(resultMap["status"] as String),
            estimatedPoints = resultMap["estimated_points"] as Int,
            realPoints = resultMap["real_points"] as Int?,
        )
    }

    override fun findByProject(projectId: Long): List<Issue> {

        try {
            val sql = "SELECT * " +
                    "FROM ${getTableName()} " +
                    "WHERE project_id = ?"
            connect()
            val statement = connection!!.prepareStatement(sql)
            statement.setInt(1, projectId.toInt())
            val resultSet = statement.executeQuery()

            return resultSetToMapList(resultSet).map(::toEntity)
        } catch (e: Exception) {
            throw PersistenceException("Error while finding issues by project", e)
        } finally {
            disconnect()
        }
    }
}