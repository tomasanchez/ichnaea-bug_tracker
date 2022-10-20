package org.ichnaea.controller

import org.ichnaea.core.mvc.controller.UIController
import org.ichnaea.core.ui.button.Button
import org.ichnaea.core.ui.data.Table
import org.ichnaea.service.UserService

@UIController
class UsersController : SideViewController() {

    private val userService = UserService()

    lateinit var oTable: Table

    // -------------------------------------------------------------
    // Lifecycle Methods
    // -------------------------------------------------------------

    override fun onInit() {

        byId("usersTable")?.let {
            oTable = it as Table
        }

        byId("registerButton")?.let {
            it as Button
            it.onClick {
                navTo("SignUp")
            }
        }

    }

    override fun onBeforeRendering() {
        updateTable()
        updateCount()
    }

    override fun onAfterRendering() {
    }

    // -------------------------------------------------------------
    // Internal Methods
    // -------------------------------------------------------------

    private fun updateTable() {
        oTable.clear()

        @Suppress("UNCHECKED_CAST")
        userService.findAll().forEach {
            oTable.addRow(it.toTableRow() as Array<Any>)
        }
    }

    private fun updateCount() {
        byId("viewTitle")?.let {
            val title = it as org.ichnaea.core.ui.text.Title
            title.text = "Users (${oTable.rowCount})"
        }
    }

}