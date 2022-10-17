package org.ichnaea.core.ui.container

import java.awt.AlphaComposite
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JPanel


open class TransparentPanel(var alpha: Float = 1f) : JPanel() {

    init {
        isOpaque = false
    }

    override fun paint(grphcs: Graphics) {
        val g2 = grphcs as Graphics2D
        g2.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha)
        super.paint(grphcs)
    }
}