package org.ichnaea.core.ui.button

import jiconfont.IconCode
import org.ichnaea.core.ui.semantic.SemanticColor
import org.jdesktop.animation.timing.Animator
import org.jdesktop.animation.timing.TimingTarget
import org.jdesktop.animation.timing.TimingTargetAdapter
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.border.EmptyBorder


class NavButton(
    text: String,
    color: Color = SemanticColor.PRIMARY,
    icon: IconCode? = null,
    var index: Int = 0,
    var isMainButton: Boolean = false,
) : SemanticButton() {

    private val effectColor = Color.RED
    private var isMouseOver = false

    init {
        this.text = text

        font =
            if (isMainButton)
                font.deriveFont(Font.BOLD, 14f)
            else font.deriveFont(Font.PLAIN, 12f)


        isContentAreaFilled = false

        setSemanticColor(color)

        border =
            if (isMainButton) EmptyBorder(0, 10, 0, 0)
            else EmptyBorder(0, 45, 0, 0)
        horizontalAlignment = LEFT

        if (!isMainButton) {

            foreground = SemanticColor.SECONDARY

            val target: TimingTarget = object : TimingTargetAdapter() {
                override fun timingEvent(fraction: Float) {
                    alpha = if (isMouseOver) fraction else 1 - fraction
                    repaint()
                }
            }

            animator = Animator(400, target)
            animator.resolution = 0

            addMouseListener(object : MouseAdapter() {

                override fun mouseEntered(e: MouseEvent?) {
                    isMouseOver = true
                    startAnimator()
                }

                override fun mouseExited(e: MouseEvent?) {
                    startAnimator()
                    isMouseOver = false
                }
            })

        } else {
            createIcon(icon)
        }
    }

    override fun paintComponent(grphcs: Graphics) {
        super.paintComponent(grphcs)
        val g2 = grphcs as Graphics2D

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        if (!isMainButton) {
            g2.color = SemanticColor.PRIMARY

            if (isSelected)
                alpha = 1f

            val size = 6
            val y = (height - size) / 2
            g2.drawOval(27, y, size + 1, size + 1)
            g2.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha)
            g2.fillOval(27, y, size + 1, size + 1);

            pressedPoint?.let {
                g2.color = effectColor
                g2.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha)
                g2.fillOval(
                    (it.x - animateSize / 2).toInt(),
                    (it.y - animateSize / 2).toInt(),
                    animateSize.toInt(),
                    animateSize.toInt()
                )
            }
        }

        g2.dispose()
    }

    override fun setSelected(wasSelected: Boolean) {
        super.setSelected(wasSelected)

        if (wasSelected || isMouseOver) {
            foreground = SemanticColor.DARK.darker()
            background = SemanticColor.LIGHT
        } else {
            alpha = 0f
            foreground = SemanticColor.SECONDARY
            background = Color.WHITE
        }

    }

    // --------------------------------
    // Animation
    // --------------------------------

    private fun startAnimator() {
        if (animator.isRunning) {
            val fraction = animator.timingFraction
            animator.stop()
            animator.startFraction = 1f - fraction
        } else
            animator.startFraction = 0f

        animator.start()
    }
}