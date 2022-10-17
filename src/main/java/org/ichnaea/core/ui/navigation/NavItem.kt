package org.ichnaea.core.ui.navigation

import jiconfont.IconCode
import net.miginfocom.swing.MigLayout
import org.ichnaea.core.ui.button.NavButton
import org.ichnaea.core.ui.semantic.SemanticColor
import org.jdesktop.animation.timing.Animator
import org.jdesktop.animation.timing.TimingTargetAdapter
import java.awt.Color
import java.awt.Graphics
import java.awt.event.ActionEvent
import javax.swing.GroupLayout
import javax.swing.JPanel


class NavItem(
    val text: String,
    val icon: IconCode? = null,
    var index: Int = 0,
    var onClear: () -> Unit = {},
    val parentLayout: MigLayout? = null,
    onClickHandler: () -> Unit = {},
    subItems: List<SubNavItem> = arrayListOf(),
    isMainButton: Boolean = false,
) : JPanel() {

    val button: NavButton
    var isOpen = false
    var alpha = 1f
    private val subItems: MutableList<NavItem> = mutableListOf()
    private lateinit var animator: Animator

    init {
        initComponents()

        isOpaque = false
        layout = MigLayout(
            "wrap, fillx, inset 0",
            "[fill]",
            "[fill,35!] ${if (hasSubItems()) "0[fill,30!]" else ""}"
        )

        button = NavButton(
            text = text,
            icon = icon,
            color = SemanticColor.LIGHT,
            isMainButton = isMainButton || subItems.isNotEmpty(),
            index = index
        )


        add(button)

        button.onClick {
            onClear()
            markSelection()
            toggleDropDown()
            onClickHandler()
        }

        var subIndex = 0

        this.subItems.addAll(subItems.map {
            NavItem(
                text = it.name,
                index = subIndex++,
                onClear = onClear,
                parentLayout = this.layout as MigLayout,
                onClickHandler = it.onClick
            )
        })

        this.subItems.forEach(this::add)

        if (hasSubItems()) {
            parentLayout?.let(::initAnimator)
        }

    }

    private fun initComponents() {
        val layout = GroupLayout(this)

        this.layout = layout

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGap(0, 400, Short.MAX_VALUE.toInt())
        )

        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGap(0, 300, Short.MAX_VALUE.toInt())
        )
    }

    /**
     * Event handler for clicking a NavItem submenu
     *
     * @param subIndex the item subIndex
     * @param action to be performed
     * @receiver a SubItem Navigation Button with the specified subIndex
     */
    fun onClick(subIndex: Int, action: (e: ActionEvent) -> Unit) {
        subItems[subIndex].onClick(action)
    }

    /**
     * Event handler for clicking a NavItem
     *
     * @param action to be performed when NavItem is clicked
     * @receiver the Navigation Button
     */
    fun onClick(action: (e: ActionEvent) -> Unit) = button.onClick { action(it) }

    fun addSubItem(item: NavItem) {
        item.index = subItems.size
        subItems.add(item)
        add(item)
    }

    private fun hasSubItems(): Boolean {
        return subItems.isNotEmpty()
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
    }

    // --------------------------------
    // Selection
    // --------------------------------

    fun clearSelection() {
        foreground = Color.WHITE

        components
            .filterIsInstance<NavItem>()
            .map(NavItem::button)
            .forEach { it.isSelected = false }

        button.isSelected = false
    }

    private fun markSelection() {
        button.isSelected = true
        if (this.parent is NavItem)
            (this.parent as NavItem).button.isSelected = true
    }

    fun toggleDropDown() {
        if (button.isMainButton && hasSubItems()) {
            isOpen = !isOpen
            startAnimator()
        }
    }

    // --------------------------------
    // Animation
    // --------------------------------

    private fun initAnimator(layout: MigLayout) {

        animator = Animator(300, object : TimingTargetAdapter() {
            private var height = 0

            override fun begin() {
                height = preferredSize.height - 35
            }

            override fun timingEvent(fraction: Float) {
                val f = if (isOpen) fraction else 1f - fraction
                val s = (35 + f * height).toInt()
                layout.setComponentConstraints(this@NavItem, "h $s!")
                revalidate()
                repaint()
            }
        })

        animator.resolution = 0
        animator.deceleration = .5f
        animator.acceleration = .5f
    }

    private fun startAnimator() {
        if (animator.isRunning) {
            animator.stop()
            animator.startFraction = 1f - animator.timingFraction
        } else
            animator.startFraction = 0f
        animator.start()
    }

}