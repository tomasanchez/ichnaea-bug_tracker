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

}