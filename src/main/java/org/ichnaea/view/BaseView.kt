package org.ichnaea.view

import org.ichnaea.core.mvc.view.View
import org.ichnaea.core.ui.data.Table
import org.slf4j.Logger
import javax.swing.JScrollPane
import javax.swing.table.DefaultTableModel

abstract class BaseView : View() {

    companion object {
        private val log: Logger = org.slf4j.LoggerFactory.getLogger(BaseView::class.java)

        fun reflect() {
            log.info("Initializing Reflections for Views")
        }

    }

    /**
     * Convenience method for adding a value in the view model without adding in to any container.
     *
     * @param key the access identifier
     * @param value component or object to be stored
     */
    fun addToModel(key: String, value: Any) {
        model[key] = value
    }

    protected fun table(
        scrollPanel: JScrollPane,
        active: Boolean = true,
        showHeader: Boolean = false,
        columns: Array<String> = arrayOf("Id", "Name"),
    ): Table {

        val table = Table(
            active = active,
            showHeader = showHeader
        )

        table.model = object : DefaultTableModel(
            arrayOf(), columns
        ) {
            override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean {
                return false
            }
        }

        table.columnModel.getColumn(0).width = 0
        table.columnModel.getColumn(0).maxWidth = 0

        scrollPanel.setViewportView(table)
        table.fixTable(scrollPanel)
        return table
    }

}