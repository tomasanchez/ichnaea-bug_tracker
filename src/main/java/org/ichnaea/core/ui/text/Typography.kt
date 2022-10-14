package org.ichnaea.core.ui.text

import org.ichnaea.core.ui.semantic.SemanticColor
import java.awt.Color
import java.awt.Font
import javax.swing.JLabel
import javax.swing.border.EmptyBorder

open class Typography(
    text: String,

    color: Color = SemanticColor.DARK,

    /**
     * Vertical Alignment.
     */
    va: Float = LEFT_ALIGNMENT,

    /**
     * Horizontal Alignment.
     */
    ha: Float = CENTER_ALIGNMENT,

    style: Int = Font.PLAIN,
) : JLabel() {

    init {
        foreground = color
        alignmentX = va
        alignmentY = ha
        font = font.deriveFont(style, 14f)
        border = EmptyBorder(0, 3, 0, 3)
        this.text = text
    }


}