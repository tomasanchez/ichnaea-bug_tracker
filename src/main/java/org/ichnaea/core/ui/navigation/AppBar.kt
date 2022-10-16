package org.ichnaea.core.ui.navigation

import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons
import org.ichnaea.core.ui.button.Button
import java.awt.Color
import java.awt.Dimension
import java.awt.FlowLayout
import javax.swing.JPanel

class AppBar : JPanel() {

    private lateinit var bars: Button

    init {
        maximumSize = Dimension(Short.MAX_VALUE.toInt(), 100)
        isOpaque = true
        background = Color.WHITE
        layout = FlowLayout(FlowLayout.LEFT, 0, 0)
        initComponents()
    }
    
    fun onBars(action: () -> Unit) = bars.onClick { action() }

    // --------------------------------
    // Components
    // --------------------------------

    private fun initComponents() {
        bars = Button(text = "", icon = GoogleMaterialDesignIcons.MENU)
        add(bars)
    }

    fun onMenu() {
        println("onMenu")
    }

}