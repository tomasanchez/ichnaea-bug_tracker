package org.ichnaea.controller

import org.ichnaea.core.mvc.controller.MVCController
import org.slf4j.Logger

abstract class BaseController : MVCController() {

    companion object {
        private val log: Logger = org.slf4j.LoggerFactory.getLogger(BaseController::class.java)

        fun reflect() {
            log.info("Initializing Reflections for controllers")
        }

        fun navTo(viewName: String) {
            log.info("Navigating to view: $viewName")
        }
    }

}