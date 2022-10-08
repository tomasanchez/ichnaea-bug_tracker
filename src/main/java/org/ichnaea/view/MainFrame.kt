package org.ichnaea.view

import java.awt.Dimension
import javax.swing.JFrame
import javax.swing.JOptionPane

class MainFrame : JFrame() {

    companion object {

        fun open(): MainFrame {
            try {
                val frame = MainFrame()
                frame.isVisible = true
                return frame
            } catch (e: Exception) {
                JOptionPane.showMessageDialog(null, e.message)
                throw e
            }
        }

        val DIMENSION: Dimension = Dimension(640, 480)
    }

    init {
        title = "Ichnaea - Issue Tracker"
        defaultCloseOperation = EXIT_ON_CLOSE
        size = DIMENSION
        setLocationRelativeTo(null)
    }

}
