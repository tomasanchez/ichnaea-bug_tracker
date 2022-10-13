package org.ichnaea.controller

import org.ichnaea.core.mvc.controller.ControllerLoader
import org.ichnaea.core.mvc.controller.MVCController
import org.ichnaea.core.ui.form.Validatable
import org.ichnaea.core.ui.semantic.Alert
import org.ichnaea.core.ui.semantic.SemanticColor
import org.slf4j.Logger
import java.awt.Color
import java.awt.Component
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

abstract class BaseController : MVCController() {

    protected var alert: Alert? = null

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
        return this.view.model[component_id] as Component?
    }

    protected fun showAlert(title: String, message: String, color: Color = SemanticColor.SUCCESS) {

        alert?.let {
            view.remove(it)
        }

        alert = Alert(
            message = message,
            title = title,
            color = color
        )

        view.set(alert)


        Executors.newSingleThreadScheduledExecutor().schedule({
            view.remove(alert)
        }, 10, TimeUnit.SECONDS)

        repaint()
    }


}