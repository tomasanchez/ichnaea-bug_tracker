package org.ichnaea.core.ui.navigation

import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons
import net.miginfocom.swing.MigLayout
import org.ichnaea.core.ui.container.ScrollBar
import org.ichnaea.core.ui.container.TransparentPanel
import org.ichnaea.core.ui.navigation.animation.SideNavAnimation
import org.ichnaea.core.ui.navigation.event.NavPopUpEvent
import org.ichnaea.core.ui.navigation.event.NavSelectionEvent
import org.ichnaea.core.ui.semantic.SemanticColor
import org.ichnaea.core.ui.text.Typography
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Font
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane


class SideNav : TransparentPanel() {

    var isShown = true

    var isNavEnabled = true

    var onSelection: NavSelectionEvent? = null

    var onPopUp: NavPopUpEvent? = null

    private val scrollPanel = JScrollPane()
    private val panel = JPanel()
    private var index = 0

    init {
        layout = BorderLayout()
        isOpaque = true
        background = Color.WHITE
        initComponents()
        add(panel)
        addGroupTitle("Navigation")
        addSpace(10)
        addItem(
            NavItem(
                text = "Home",
                icon = GoogleMaterialDesignIcons.HOME,
                subItems = arrayListOf("Mozilla", "Chromium"),
            )
        )
    }

    fun hideNav() {
        isNavEnabled = false
        if (isShown) hideItems()
    }


    // --------------------------------
    // Items
    // --------------------------------

    /**
     * Adds an item to the navigation.
     *
     * @param item a navigation entry
     */
    fun addItem(item: NavItem) {
        panel.add(item, "h 40!")

        index++
    }

    /**
     * Adds a space between items.
     *
     * @param size the space size
     */
    fun addSpace(size: Int) {
        panel.add(JLabel(), "h $size!")
    }

    fun addGroupTitle(groupTitle: String) {
        val title = Typography(text = groupTitle, color = SemanticColor.SECONDARY, style = Font.BOLD)
        panel.add(title)
    }

    /**
     * Hides all items in the navigation.
     */
    private fun hideItems() {
        panel.components
            .filterIsInstance<NavItem>()
            .filter { it.isOpen }
            .forEach {
                it.isOpen = false
                SideNavAnimation(panel.layout as MigLayout, it, 500).closeMenu()
            }
    }

    fun clearSelected() {
        components
            .filterIsInstance<NavItem>()
            .forEach(NavItem::clearSelection)
    }

    fun setSelectedIndex(index: Int, subIndex: Int) {
        components
            .filterIsInstance<NavItem>()
            .find { it.index == index }?.setSelectedIndex(subIndex)
    }

    // --------------------------------
    // Components Initialization
    // --------------------------------

    private fun initComponents() {
        initScrollPane()
        initPanel()
    }


    private fun initPanel() {
        panel.isOpaque = false
        panel.border = null
        panel.layout = MigLayout(
            "wrap, fillx, insets 0",
            "[fill]",
            "[]0[]"
        )
        scrollPanel.setViewportView(panel)
    }

    private fun initScrollPane() {
        scrollPanel.border = null
        scrollPanel.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        scrollPanel.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        scrollPanel.viewportBorder = null
        scrollPanel.verticalScrollBar = ScrollBar()
    }


}