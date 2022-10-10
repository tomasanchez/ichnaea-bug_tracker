package org.ichnaea.core.mvc.controller

import org.ichnaea.controller.BaseController
import org.ichnaea.core.mvc.view.ViewLoader
import org.reflections.Reflections
import org.slf4j.Logger
import java.lang.reflect.Modifier

object ControllerLoader {

    private val controllers: MutableMap<String, MVCController> = HashMap()

    private val logger: Logger = org.slf4j.LoggerFactory.getLogger(ControllerLoader::class.java)

    fun loadControllers(classPath: String = "org.ichnaea.controller") {

        logger.debug("Loading controllers from $classPath")
        BaseController.reflect()

        val reflections = Reflections(classPath)
        var loaded = 0
        val classes =
            reflections.getSubTypesOf(MVCController::class.java)
                .filter { clazz ->
                    clazz.isAnnotationPresent(Controller::class.java) && !Modifier.isAbstract(clazz.modifiers)
                }

        classes.forEach { clazz ->
            try {
                val controller = clazz.newInstance()
                controllers[controller.name] = controller
                val view = ViewLoader.findView(controller.name)
                controller.setView(view)
                loaded++
                logger.trace("Loaded controller ${controller.name}")
            } catch (e: Exception) {
                logger.error("Failed to load controller ${clazz.name}", e)
            }
        }

        logger.info("Loaded $loaded controllers")
    }

    fun getController(name: String): MVCController? {
        return controllers[name]
    }


}