package org.ichnaea.view

import net.miginfocom.swing.MigLayout
import org.ichnaea.core.mvc.view.MVCView
import org.ichnaea.core.ui.semantic.SemanticColor
import java.awt.Color
import java.awt.Component
import javax.swing.BoxLayout
import javax.swing.JPanel
import javax.swing.border.EmptyBorder

abstract class SideView : BaseView() {

    protected val containerPanel = JPanel()

    init {
        initPanel()
        initContainerPanel()
        panel.add(containerPanel, "align center, wrap")
    }


    // -----------------------------
    // Initialization
    // -----------------------------

    private fun initPanel() {
        panel.layout = MigLayout("fill, insets 0", "[fill]", "0[fill]0[fill]15[fill]")
        panel.border = EmptyBorder(15, 15, 15, 15)
        panel.isOpaque = true
        panel.background = SemanticColor.LIGHT
    }

    private fun initContainerPanel() {
        containerPanel.layout = BoxLayout(containerPanel, BoxLayout.PAGE_AXIS)
        containerPanel.border = EmptyBorder(10, 10, 10, 10)
        containerPanel.background = Color.WHITE
    }

    // -----------------------------
    // Internal Methods
    // -----------------------------

    override fun isFullScreen(): Boolean {
        return false
    }

    override fun set(value: Component): MVCView {
        containerPanel.add(value, "wrap, align center")
        return this
    }

    override fun set(key: String, value: Component): MVCView {
        set(value)
        model[key] = value
        return this
    }

    override fun remove(component: Component) {
        containerPanel.remove(component)
        containerPanel.revalidate()
        containerPanel.repaint()
        panel.remove(component)
        repaint()
    }

    override fun repaint() {
        super.repaint()
        containerPanel.revalidate()
        containerPanel.repaint()
    }

}