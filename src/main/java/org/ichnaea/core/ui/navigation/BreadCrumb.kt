package org.ichnaea.core.ui.navigation

import org.ichnaea.core.ui.semantic.SemanticColor
import org.ichnaea.core.ui.text.Typography
import java.awt.FlowLayout
import java.awt.Font
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

class BreadCrumb(
    private val links: MutableList<Typography> = mutableListOf(),
) : JPanel() {

    init {

        isOpaque = false
        border = EmptyBorder(10, 0, 10, 0)
        layout = FlowLayout(FlowLayout.LEFT, 0, 0)

        val lastLink = links.last()

        links.forEach { link ->
            add(link)

            if (link != lastLink) {
                add(Typography(text = " / ", style = Font.BOLD, color = SemanticColor.SECONDARY))
            }

        }

    }

}