package org.ichnaea.controller

import org.ichnaea.core.mvc.controller.ControllerLoader
import org.ichnaea.core.mvc.controller.MVCController
import org.ichnaea.core.ui.form.Validatable
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

        fun validateEmpty(value: String, input: Validatable): Boolean {

            val hasError = value.isEmpty() || value.isBlank()

            input.setError(hasError)
            
            return hasError
        }


    }

    protected fun byId(component_id: String): Component? {
        return this.mvcView.model[component_id] as Component?
    }


}