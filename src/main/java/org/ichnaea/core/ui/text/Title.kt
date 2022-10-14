package org.ichnaea.core.ui.text

import org.ichnaea.core.ui.semantic.SemanticColor
import java.awt.Color
import java.awt.Font

class Title(
    text: String,
    level: TitleLevel = TitleLevel.H1,
    color: Color = SemanticColor.DARK,
    va: Float = CENTER_ALIGNMENT,
    ha: Float = CENTER_ALIGNMENT,
) : Typography(text = text, va = va, ha = ha, color = color) {

    init {

        var isBold = false

        val size =
            when (level) {
                TitleLevel.H1 -> 22f.also { isBold = true }
                TitleLevel.H2 -> 20f.also { isBold = true }
                TitleLevel.H3 -> 18f.also { isBold = true }
                TitleLevel.H4 -> 16f
                TitleLevel.H5 -> 14f
            }

        font = font.deriveFont(
            if (isBold) Font.BOLD else Font.PLAIN,
            size
        )
    }

}


