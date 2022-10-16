package org.ichnaea.core.ui.navigation

import jiconfont.IconCode
import net.miginfocom.swing.MigLayout
import org.ichnaea.core.ui.button.NavButton
import org.ichnaea.core.ui.navigation.event.NavSelectionEvent
import org.ichnaea.core.ui.semantic.SemanticColor
import org.jdesktop.animation.timing.Animator
import org.jdesktop.animation.timing.TimingTargetAdapter
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.geom.AffineTransform
import java.awt.geom.Path2D
import javax.swing.JPanel


class NavItem(
    val text: String,
    val icon: IconCode? = null,
    var index: Int = 0,
    subItems: List<String> = arrayListOf(),
) : JPanel() {

    val button: NavButton
    var isOpen = false
    var onSelection: NavSelectionEvent? = null
    var alpha = 1f
    private val subItems: MutableList<NavItem> = mutableListOf()
    private var buttonAngle = -1
    private lateinit var animator: Animator

    init {
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
            isMainButton = subItems.isNotEmpty(),
            index = index
        )

        button.addMouseListener(object : MouseAdapter() {

            override fun mouseEntered(e: MouseEvent?) {
                cursor = Cursor(Cursor.HAND_CURSOR)
                button.foreground = SemanticColor.PRIMARY
            }

            override fun mouseExited(e: MouseEvent?) {
                if (!button.isSelected) {
                    button.foreground = SemanticColor.DARK
                }
            }
        })

        add(button)
        var subIndex = 1

        this.subItems.addAll(subItems.map { NavItem(text = it, index = subIndex++) })
        this.subItems.forEach(this::add)

        if (hasSubItems()) {
            buttonAngle = 0
            initAnimator(this.layout as MigLayout)
        }


    }

    fun onClick(action: (e: ActionEvent) -> Unit) = button.onClick { action(it) }

    fun addSubItem(item: NavItem) {
        subItems.add(item)
        add(item)
    }

    private fun hasSubItems(): Boolean {
        return subItems.isNotEmpty()
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        if (buttonAngle >= 0) {
            val g2 = g.create() as Graphics2D
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            g2.color = foreground
            val x: Double = width + 10 * 1.0
            val y = 15.0
            val p2: Path2D = Path2D.Double()
            p2.moveTo(x, y)
            p2.lineTo(x + 4, y + 4)
            p2.lineTo(x + 8, y)
            val at = AffineTransform.getRotateInstance(
                Math.toRadians(buttonAngle.toDouble()),
                (x + 4),
                (y + 2)
            )
            g2.stroke = BasicStroke(1.8f)
            g2.draw(at.createTransformedShape(p2))
            g2.dispose()
        }
    }

    // --------------------------------
    // Selection
    // --------------------------------

    fun clearSelection() {
        foreground = Color.WHITE
        components
            .filterIsInstance<NavButton>()
            .forEach { it.isSelected = false }
    }

    fun setSelectedIndex(index: Int) {
        components
            .filterIsInstance<NavButton>()
            .forEach {

                if (it.isMainButton) {
                    it.isSelected = true
                    it.foreground = SemanticColor.LIGHT
                }

                if (it.index == index) {
                    it.isSelected = true
                    return@forEach
                }
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
                buttonAngle = (f * 180).toInt()
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