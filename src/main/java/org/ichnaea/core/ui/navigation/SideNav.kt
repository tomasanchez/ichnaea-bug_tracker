package org.ichnaea.core.ui.navigation

import net.miginfocom.swing.MigLayout
import org.ichnaea.core.ui.avatar.BrandLogo
import org.ichnaea.core.ui.container.ScrollBar
import org.ichnaea.core.ui.container.TransparentPanel
import org.ichnaea.core.ui.navigation.animation.SideNavAnimation
import org.ichnaea.core.ui.semantic.SemanticColor
import org.ichnaea.core.ui.text.Typography
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Font
import javax.swing.GroupLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.border.EmptyBorder


class SideNav(
    title: String?,
    brandImage: String?,
    color: Color = Color.WHITE,
) : TransparentPanel() {

    private var logo: BrandLogo? =
        title?.takeUnless { brandImage == null }?.let { BrandLogo(text = it, imagePath = brandImage) }

    var isShown = true

    var isNavEnabled = true

    val menuLayout: MigLayout

    private val scrollPanel = JScrollPane()

    private val panel = JPanel()

    private var index = 0

    init {
        layout = BorderLayout()
        isOpaque = true
        background = color
        menuLayout = MigLayout(
            "wrap, fillx, insets 0",
            "[fill]",
            "[]0[]"
        )

        initComponents()
    }

    fun hideNav() {
        isNavEnabled = false
        if (isShown) hideItems()
    }


    // --------------------------------
    // Items
    // --------------------------------

    fun addGroup(groupName: String, vararg items: NavItem) {
        addGroupTitle(groupName)
        addSpace(10)
        items.forEach(::addItem)
    }


    /**
     * Adds an item to the navigation.
     *
     * @param item a navigation entry
     */
    fun addItem(item: NavItem) {
        item.index = index
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
        title.border = EmptyBorder(15, 15, 5, 5)
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
        panel.components
            .filterIsInstance<NavItem>()
            .forEach(NavItem::clearSelection)
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


        val panelLayout = GroupLayout(panel)

        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 312, Short.MAX_VALUE.toInt())
        )

        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 523, Short.MAX_VALUE.toInt())
        )

        panel.layout = panelLayout

        scrollPanel.setViewportView(panel)
        scrollPanel.viewport.isOpaque = false

        val layout = GroupLayout(this)
        this.layout = layout

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(scrollPanel, GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE.toInt())
                .addComponent(logo, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE.toInt())
        )

        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(
                            logo,
                            GroupLayout.PREFERRED_SIZE,
                            GroupLayout.DEFAULT_SIZE,
                            GroupLayout.PREFERRED_SIZE
                        )
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrollPanel, GroupLayout.DEFAULT_SIZE, 523, Short.MAX_VALUE.toInt())
                )
        )

        panel.layout = menuLayout
    }

    private fun initScrollPane() {
        scrollPanel.border = null
        scrollPanel.isOpaque = false
        scrollPanel.horizontalScrollBarPolicy = JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        scrollPanel.verticalScrollBarPolicy = JScrollPane.VERTICAL_SCROLLBAR_ALWAYS
        scrollPanel.viewportBorder = null
        scrollPanel.verticalScrollBar = ScrollBar()
    }


}