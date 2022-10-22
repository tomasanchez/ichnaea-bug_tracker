package org.ichnaea.core.db

import org.ichnaea.core.exception.ConnectionException
import org.slf4j.Logger
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException


abstract class SQLiteConnector(
    private val url: String = "jdbc:sqlite:sqlite/sample.db",
    private val time_out: Int = 30,
) {

    companion object {
        private val LOGGER: Logger = org.slf4j.LoggerFactory.getLogger(SQLiteConnector::class.java)
    }

    var connection: Connection? = null

    // ---------------------------------------------------------------------
    // DB Connection
    // ---------------------------------------------------------------------

    protected fun connect() {

        try {
            connection = DriverManager.getConnection(url)
        } catch (e: SQLException) {
            LOGGER.error("Connection Error")
            throw ConnectionException("Trouble connecting to DataBase. Maybe, try again later.", e)
        }

    }

    protected fun createStatement(): java.sql.Statement? {
        return try {
            connection?.createStatement()?.apply {
                queryTimeout = time_out
            }
        } catch (e: SQLException) {
            LOGGER.error("Statement Error")
            throw ConnectionException("Trouble creating Statement. Maybe, try again later.", e)
        }
    }

    protected fun createPreparedStatement(sql: String): java.sql.PreparedStatement? {
        return try {
            return connection?.prepareStatement(sql)
        } catch (e: SQLException) {
            LOGGER.error("Connection Error")
            throw ConnectionException("Trouble closing Connection. Maybe, try again later.", e)
        }
    }


    protected fun executeStatement(query: String): java.sql.ResultSet {
        return try {
            val statement = connection!!.createStatement()
            return statement.executeQuery(query)
        } catch (e: SQLException) {
            LOGGER.error("Query Error")
            throw ConnectionException("Trouble executing Query.", e)
        } catch (e: Exception) {
            LOGGER.error("Connection Error")
            throw ConnectionException("Trouble executing Query.", e)
        }
    }

    protected fun disconnect() {
        try {
            connection?.close()
        } catch (e: SQLException) {
            LOGGER.error("Disconnection Error")
            throw ConnectionException("Trouble disconnecting from DataBase. Maybe, try again later.", e)
        }
    }

    // ---------------------------------------------------------------------
    // Fetching Data
    // ---------------------------------------------------------------------

    protected fun runQuery(sql: String): List<Map<String, Any>> {

        connect()

        val rs = createStatement()?.executeQuery(sql)

        val resultList = resultSetToMapList(rs)

        disconnect()

        return resultList
    }

    protected fun resultSetToMapList(rs: java.sql.ResultSet?): List<Map<String, Any>> {

        val resultList: MutableList<MutableMap<String, Any>> = ArrayList()

        rs?.let {
            while (it.next()) {
                val columnCount = it.metaData.columnCount

                val objectMap: MutableMap<String, Any> = HashMap()

                for (i in 1..columnCount) {
                    val columnName = it.metaData.getColumnName(i).lowercase()
                    objectMap[columnName] = it.getObject(i)
                }

                resultList.add(objectMap)
            }
        }

        return resultList
    }

}