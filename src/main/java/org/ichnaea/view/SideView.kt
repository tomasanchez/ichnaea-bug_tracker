package org.ichnaea.view

import net.miginfocom.swing.MigLayout
import org.ichnaea.core.ui.semantic.SemanticColor
import java.awt.Color
import javax.swing.BoxLayout
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

abstract class SideView : BaseView() {

    protected val containerPanel = JPanel()

    init {
        panel.layout = MigLayout("fill, insets 0", "[fill]", "[fill]")
        panel.border = EmptyBorder(15, 15, 15, 15)
        panel.isOpaque = true
        panel.background = SemanticColor.LIGHT
        containerPanel.layout = BoxLayout(containerPanel, BoxLayout.PAGE_AXIS)
        containerPanel.border = EmptyBorder(10, 10, 10, 10)
        containerPanel.background = Color.WHITE
        panel.add(containerPanel)
    }

    override fun isFullScreen(): Boolean {
        return false
    }

}