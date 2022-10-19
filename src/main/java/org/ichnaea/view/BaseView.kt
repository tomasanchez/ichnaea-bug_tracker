package org.ichnaea.view

import org.ichnaea.core.mvc.view.MVCView
import org.slf4j.Logger

abstract class BaseView : MVCView() {

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

}