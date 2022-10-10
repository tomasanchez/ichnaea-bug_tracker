package org.ichnaea.core.ui.text

import org.ichnaea.core.ui.semantic.SemanticColor
import java.awt.Color
import javax.swing.JLabel

enum class TitleLevel {
    H1, H2, H3, H4, H5, H6
}

class Title(
    text: String,
    level: TitleLevel = TitleLevel.H1,
    color: Color = SemanticColor.DARK,
    xPosition: Float = CENTER_ALIGNMENT,
) : JLabel() {

    init {

        font = font.deriveFont(
            when (level) {
                TitleLevel.H1 -> 24f
                TitleLevel.H2 -> 20f
                TitleLevel.H3 -> 16f
                TitleLevel.H4 -> 14f
                TitleLevel.H5 -> 12f
                TitleLevel.H6 -> 10f
            }
        )

        foreground = color
        setText(text)
        alignmentX = xPosition

    }

}


