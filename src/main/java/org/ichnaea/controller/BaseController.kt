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

    protected var pathId: Long = 0

    companion object {
        private val log: Logger = org.slf4j.LoggerFactory.getLogger(BaseController::class.java)

        fun reflect() {
            log.info("Initializing Reflections for controllers")
        }

        /**
         * Navigates to the View with the given name.
         *
         * @param viewName the name of the View (e.g. "login"), case insensitive
         */
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

    /**
     * Navs to a view with the given name and the given pathId.
     *
     * @param viewName the name of the View (e.g. "projects"), case insensitive
     * @param pathId an Entity id to be passed to the view
     */
    fun navTo(viewName: String, pathId: Long) {
        val ctrl = ControllerLoader.getController(viewName) as BaseController?
        ctrl?.onPathChange(pathId)
        this.pathId = pathId
        ctrl?.show()
    }

    /**
     * Event Handler when the pathId changes.
     *
     * @param id an Entity Id
     */
    open fun onPathChange(id: Long) {

    }


    /**
     * Obtains a component from the ViewModel
     *
     * @param component_id The unique identifier assigned to the component
     * @return Possible a component if found, otherwise null
     */
    protected fun byId(component_id: String): Component? {
        return this.view.model[component_id] as Component?
    }

    /**
     * Shows an alert banner
     *
     * @param title The title of the alert
     * @param message Some additional information
     * @param color The background color
     */
    protected open fun showAlert(title: String, message: String, color: Color = SemanticColor.SUCCESS) {

        removeAlert()

        alert = Alert(
            message = message,
            title = title,
            color = color
        )

        view.set(alert, "align center, wrap, h 70!, w 60%")


        Executors.newSingleThreadScheduledExecutor().schedule({
            removeAlert()
        }, 10, TimeUnit.SECONDS)

        repaint()
    }

    /**
     * Removes the alert from the view if it exists.
     */
    protected open fun removeAlert() {
        alert?.let {
            view.remove(it)
            alert = null
        }
    }

}