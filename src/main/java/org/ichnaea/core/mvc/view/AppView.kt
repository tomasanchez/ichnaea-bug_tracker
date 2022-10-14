package org.ichnaea.core.mvc.view

import java.awt.Dimension
import javax.swing.ImageIcon
import javax.swing.JFrame
import javax.swing.JOptionPane

class AppView() : JFrame() {

    companion object {

        fun open(): AppView {
            val frame = AppView()
            frame.launch()
            return frame
        }

        val DIMENSION: Dimension = Dimension(720, 600)
    }

    init {
        title = "Ichnaea - Issue Tracker"
        defaultCloseOperation = EXIT_ON_CLOSE
        size = DIMENSION
        iconImage = ImageIcon(javaClass.getResource("/icon/ichnaea-icon.png")).image
        setLocationRelativeTo(null)
    }

    fun launch() {
        try {
            this.isVisible = true
            preferredSize = DIMENSION
            this.pack()
        } catch (e: Exception) {
            JOptionPane.showMessageDialog(null, e.message)
            throw e
        }
    }

}
