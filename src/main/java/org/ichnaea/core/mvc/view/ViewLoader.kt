package org.ichnaea.core.mvc.view

import org.ichnaea.core.ui.app.AppUI
import org.ichnaea.view.BaseView
import org.reflections.Reflections
import org.slf4j.Logger
import java.lang.reflect.Modifier

object ViewLoader {

    private val views: MutableMap<String, MVCView> = HashMap()

    private val logger: Logger = org.slf4j.LoggerFactory.getLogger(ViewLoader::class.java)

    fun loadViews(frame: AppUI, classPath: String = "org.ichnaea.view") {
        logger.debug("Loading views from $classPath")
        BaseView.reflect()
        val reflections = Reflections(classPath)
        var loaded = 0
        val classes =
            reflections.getSubTypesOf(MVCView::class.java)
                .filter { clazz ->
                    clazz.isAnnotationPresent(UIView::class.java) && !Modifier.isAbstract(clazz.modifiers)
                }

        classes.forEach { clazz ->
            try {
                val view = clazz.newInstance()
                views[view.name] = view
                view.setFrame(frame)
                loaded++
                logger.trace("Loaded view ${view.name}")
            } catch (e: Exception) {
                logger.error("Failed to load view ${clazz.name}", e)
            }
        }

        logger.info("Loaded $loaded views")
    }

    fun findView(name: String): MVCView? {
        return views[name]
    }

}