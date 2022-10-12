package org.ichnaea.core.ui.container

import java.awt.Color
import java.awt.Component
import java.awt.Dimension
import java.awt.FlowLayout
import javax.swing.Box
import javax.swing.JPanel

class Toolbar(
    alignment: Int = FlowLayout.CENTER,
    color: Color = Color.WHITE,
    private val spacing: Int = 25,
) : JPanel() {

    private val items = mutableListOf<Component>()

    init {
        layout = FlowLayout(alignment)
        background = color
    }

    override fun add(component: Component): Component? {

        if (items.isNotEmpty()) {
            super.add(Box.createRigidArea(Dimension(spacing, 45)))
        }

        items += component

        return super.add(component)
    }

}
