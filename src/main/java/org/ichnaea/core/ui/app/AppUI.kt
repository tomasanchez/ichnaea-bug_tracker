package org.ichnaea.core.ui.app

import javax.swing.JFrame
import javax.swing.JPanel

abstract class AppUI : JFrame() {

    abstract fun getBody(): JPanel
    abstract fun setBody(body: JPanel, isFullScreen: Boolean)
    abstract fun showBody(show: Boolean)

}
