package org.ichnaea.core.ui.text

import org.ichnaea.core.ui.semantic.SemanticColor
import java.awt.Font
import java.awt.Graphics
import javax.swing.border.EmptyBorder

class TableHeader(
    header: String,
) : Typography(text = header, style = Font.BOLD) {

    init {
        border = EmptyBorder(10, 5, 10, 5)
    }

    override fun paintComponents(g: Graphics) {
        super.paintComponents(g)
        g.color = SemanticColor.LIGHT
        g.drawLine(0, height - 1, width, height - 1)
    }

}