package org.ichnaea.core.ui.text

import org.ichnaea.core.ui.semantic.SemanticColor
import java.awt.Color
import java.awt.Cursor
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent

class Link(
    text: String,
    val tooltip: String? = null,
    val color: Color = SemanticColor.PRIMARY,
    size: Float = 14f,
    var onClick: (MouseEvent) -> Unit = {},
) : Typography(text = text, color = color, size = size) {


    init {

        tooltip?.let { toolTipText = it }

        addMouseListener(object : MouseAdapter() {

            override fun mouseEntered(e: MouseEvent) {
                cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
                foreground = color.darker()
            }

            override fun mouseExited(e: MouseEvent) {
                cursor = Cursor.getDefaultCursor()
                foreground = color
            }

            override fun mouseClicked(e: MouseEvent) = onClick(e)

        })

    }


}