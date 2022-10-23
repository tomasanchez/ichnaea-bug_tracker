package org.ichnaea.dao.sqlite

import org.ichnaea.core.db.SQLiteConnector
import org.ichnaea.core.exception.ConnectionException
import org.ichnaea.core.exception.PersistenceException
import org.ichnaea.dao.PersistentEntityDAO
import org.ichnaea.model.PersistentEntity
import java.lang.reflect.ParameterizedType
import java.sql.Timestamp
import java.time.LocalDateTime
import java.util.*


abstract class SQLiteDAO<T : PersistentEntity> : SQLiteConnector(), PersistentEntityDAO<T> {

    /**
     * Entity mapper function which allows a transformation of a resultMap into a Persistent Entity.
     *
     *
     * @param resultMap the result map from the database [key, value] pairs.
     * @return a recovered Persistent Entity.
     */
    abstract fun entityMapper(resultMap: Map<String, Any>): T

    /**
     * Kotlin helper to retrieve a complete instance of the generic type.
     *
     * @param entity the entity to be completed.
     * @param resultMap the result map from the database [key, value] pairs.
     * @return an Entity with all required fields completed.
     */
    private fun completeEntity(entity: T, resultMap: Map<String, Any>): T {

        try {
            entity.id = (resultMap["id"] as Int).toLong()
        } catch (e: Exception) {
            entity.id = null
        }

        try {
            entity.createdAt = (resultMap["created_at"] as Timestamp).toLocalDateTime()
        } catch (e: Exception) {
            entity.createdAt = LocalDateTime.now()
        }

        return entity
    }

    /**
     *  Transform a Map into an Entity
     *
     * @param resultMap the result map from the database [key, value] pairs.
     * @return a Persistent Entity.
     */
    protected fun toEntity(resultMap: Map<String, Any>): T {
        return completeEntity(entityMapper(resultMap), resultMap)
    }

    /**
     * Retrieves Table name (class name).
     *
     * <br></br>
     *
     * ? Example: PersistentEntitySet of User. Class => Table name is USER
     *
     * @return the table name
     */
    protected open fun getTableName(): String {
        return getEntityName().uppercase()
    }

    protected open fun getEntityName(): String {
        return ((javaClass.genericSuperclass as ParameterizedType)
            .actualTypeArguments[0] as Class<*>).simpleName
    }

    // ---------------------------------------------------------------------
    // CRUD Methods
    // ---------------------------------------------------------------------

    /**
     * Convenience method for SELECT * FROM {TABLE_NAME}.
     *
     * @return an SQL select query.
     */
    protected fun selectAllFromTable(): String = "SELECT * FROM ${getTableName()} T"

    /**
     * Counts the number of rows in a table.
     *
     * @return the number of rows in a table or -1 if an error occurs.
     */
    override fun count(): Int {
        try {
            connect()
            val statement = createStatement()
            val query = "SELECT COUNT(*) FROM ${getTableName()}"
            return try {
                val resultSet = statement!!.executeQuery(query)
                resultSet.getInt(1)
            } catch (e: Exception) {
                -1
            } finally {
                disconnect()
            }
        } catch (e: ConnectionException) {
            throw PersistenceException("Error counting rows in table ${getTableName()}.", e)
        }
    }

    /**
     * Retrieves all rows from a table.
     *
     * @return a list of Persistent Entities.
     */
    override fun findAll(): List<T> {

        val query = selectAllFromTable()

        try {
            val rs = runQuery(query)
            return rs.map(::toEntity)
        } catch (e: ConnectionException) {
            throw PersistenceException("Error retrieving all rows from table ${getTableName()}.", e)
        }
    }

    /**
     * Find all related foreign keys from a table. Useful for x-to-many relationships.
     *
     * @param id to be used as foreign key.
     * @param relatedTable in which the foreign key is located.
     * @return a list of related foreign keys.
     */
    fun findRelatedFor(id: Long, relatedTable: String): List<Map<String, Long>> {

        val columnId = "${getEntityName().lowercase()}_id"

        val query = "SELECT * FROM ? T WHERE T.$columnId = ?"

        try {
            val preparedStatement = createPreparedStatement(query)
            preparedStatement?.setString(1, relatedTable)
            preparedStatement?.setInt(2, id.toInt())
            val rs = preparedStatement?.executeQuery()
            @Suppress("UNCHECKED_CAST")
            return resultSetToMapList(rs) as List<Map<String, Long>>
        } catch (e: java.lang.Exception) {
            throw PersistenceException("Error retrieving all rows from table ${getTableName()}.", e)
        }
    }


    /**
     * Selects a single entity where the id matches the given id.
     *
     * @param id to be searched.
     * @return a Persistent Entity if found, null otherwise.
     */
    override fun findById(id: Long): Optional<T> {

        val query = "${selectAllFromTable()} WHERE T.id = ?"
        try {
            connect()

            val rs = createPreparedStatement(query)?.apply {
                setLong(1, id)
            }?.executeQuery()

            val resultList = resultSetToMapList(rs).map(::toEntity)

            disconnect()

            val result = resultList.firstOrNull()

            return result?.let { Optional.of(it) } ?: Optional.empty()
        } catch (e: ConnectionException) {
            throw PersistenceException("Error retrieving row from table ${getTableName()} with id $id.", e)
        }
    }

    /**
     * Selects entities with ids in a range.
     *
     * @param ids the range of ids to be searched.
     * @return a list of Persistent Entities.
     */
    override fun findByIds(ids: List<Long>): List<T> {

        val query = "${selectAllFromTable()} WHERE T.id IN (?)"
        try {
            connect()

            val rs = createPreparedStatement(query)?.apply {
                setString(1, ids.joinToString(","))
            }?.executeQuery()

            val resultList = resultSetToMapList(rs).map(::toEntity)

            disconnect()

            return resultList
        } catch (e: ConnectionException) {
            throw PersistenceException("Error retrieving row from table ${getTableName()} with id $ids.", e)
        }
    }

    /**
     * Persists an entity into the database.
     *
     * @param entity to be persisted.
     * @return the entity with its persisted data.
     */
    override fun save(entity: T): T {
        try {
            return if (entity.id == null) {
                create(entity)
            } else {
                update(entity)
            }
        } catch (e: ConnectionException) {
            throw PersistenceException("Error persisting entity $entity.", e)
        }

    }

    /**
     * Deletes a row from a table where the id matches the given id.
     *
     * @param id to be searched.
     */
    override fun delete(id: Long) {
        try {
            connect()
            val query = "DELETE FROM ${getTableName()} WHERE id = ?"
            val statement = createPreparedStatement(query)
            statement?.setInt(1, id.toInt())
            statement?.executeUpdate()
            disconnect()
        } catch (e: ConnectionException) {
            throw PersistenceException("Error deleting entity with id $id.", e)
        }
    }

    // ---------------------------------------------------------------------
    // Internal Methods
    // ---------------------------------------------------------------------

    private fun create(entity: T): T {

        val map = entity.toMap()

        val columnNames = getColumnNames(map)
        val columnValues = getColumnValues(map)

        val query = "INSERT INTO ${getTableName()} $columnNames VALUES $columnValues"

        connect()

        val preparedStatement = createPreparedStatement(query)

        preparedStatement?.let {
            setValues(it, map)
        }


        var row: Int = 0

        try {
            row = preparedStatement!!.executeUpdate()
        } catch (e: Exception) {
            throw PersistenceException("Error creating entity: ${e.message}")
        } finally {
            if (row != 1)
                disconnect()
        }


        if (row == 1) {
            try {
                val rs = preparedStatement!!.generatedKeys

                if (rs.next()) {
                    entity.id = rs.getLong(1)
                } else {
                    throw IllegalStateException("No keys were generated")
                }

            } catch (e: Exception) {
                disconnect()
                throw PersistenceException("Could not Persist", e)
            }
        } else {
            disconnect()
            throw PersistenceException("Could not Persist: No rows were affected")
        }

        disconnect()

        return entity
    }

    private fun update(entity: T): T {

        val map = entity.toMap()

        val columnNames = map.keys.joinToString(", ", "", "", transform = { "$it = ?" })

        val query = "UPDATE ${getTableName()} SET $columnNames WHERE id = ?"

        connect()

        val preparedStatement = createPreparedStatement(query)

        preparedStatement?.let {
            setValues(it, map)
            it.setLong(map.values.size + 1, entity.id!!)
        }

        var row: Int = 0

        try {
            row = preparedStatement!!.executeUpdate()
        } catch (e: Exception) {
            throw PersistenceException("Error updating entity: ${e.message}")
        } finally {

            disconnect()
            if (row != 1)
                throw PersistenceException("Could not Update: No rows were affected")

        }

        return entity
    }

    // ---------------------------------------------------------------------
    // Internal Methods
    // ---------------------------------------------------------------------


    /**
     * Joins a Map into a String of column names. e.g. (id=1L, name="Some", email="e@mail.com") => (id, name, email)
     *
     * @param map a Map of [columnName, value] pairs.
     * @return a String of column names.
     */
    protected fun getColumnNames(map: Map<String, Any>): String {
        return map.keys.joinToString(", ", "(", ")")
    }

    /**
     * Get column values from a Map.
     * e.g. (id=1L, name="Some", email="email) => (?, ?, ?) to be set in a prepared statement.
     *
     * @param map a Map of [columnName, value] pairs.
     * @return a String of column values to be set in a prepared statement.
     */
    protected fun getColumnValues(map: Map<String, Any>): String {
        return map.values.joinToString(", ", "(", ")", transform = { "?" })
    }

    /**
     * Set values in a prepared statement.
     *
     * @param preparedStatement to be set
     * @param map containing the values to be set.
     */
    protected fun setValues(preparedStatement: java.sql.PreparedStatement, map: Map<String, Any>) {
        map.values.forEachIndexed { index, value ->
            when (value) {
                is String -> preparedStatement.setString(index + 1, value)
                is Int -> preparedStatement.setInt(index + 1, value)
                is Long -> preparedStatement.setLong(index + 1, value)
                is Double -> preparedStatement.setDouble(index + 1, value)
                is Float -> preparedStatement.setFloat(index + 1, value)
                is Boolean -> preparedStatement.setBoolean(index + 1, value)
                is LocalDateTime -> preparedStatement.setTimestamp(index + 1, Timestamp.valueOf(value))
                else -> throw PersistenceException("Cannot Persist: Type not supported")
            }
        }
    }

}