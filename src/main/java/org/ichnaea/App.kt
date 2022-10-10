package org.ichnaea

import org.ichnaea.core.mvc.controller.ControllerLoader
import org.ichnaea.core.mvc.view.AppView
import org.ichnaea.core.mvc.view.ViewLoader
import java.awt.EventQueue

fun main() = run {

    EventQueue.invokeLater(::launch)
}

fun launch() {
    val frame = AppView.open()
    ViewLoader.loadViews(frame)
    ControllerLoader.loadControllers()
    ControllerLoader.getController("SignIn")?.show()
}
