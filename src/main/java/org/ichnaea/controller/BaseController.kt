package org.ichnaea.controller

import org.ichnaea.core.mvc.controller.ControllerLoader
import org.ichnaea.core.mvc.controller.MVCController
import org.slf4j.Logger
import java.awt.Component

abstract class BaseController : MVCController() {

    companion object {
        private val log: Logger = org.slf4j.LoggerFactory.getLogger(BaseController::class.java)

        fun reflect() {
            log.info("Initializing Reflections for controllers")
        }

        fun navTo(viewName: String) {
            log.info("Displaying: $viewName")
            ControllerLoader.getController(viewName)?.show()
        }

    }

    fun byId(component_id: String): Component? {
        return this.mvcView.model[component_id] as Component?
    }

}