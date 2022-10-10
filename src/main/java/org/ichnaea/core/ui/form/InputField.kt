package org.ichnaea.core.ui.form

import org.ichnaea.core.ui.semantic.SemanticColor
import org.jdesktop.animation.timing.Animator
import org.jdesktop.animation.timing.TimingTarget
import org.jdesktop.animation.timing.TimingTargetAdapter
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JTextField
import javax.swing.border.EmptyBorder


class InputField(
    val label: String,
    private val required: Boolean = true,
) : JTextField() {

    private lateinit var animator: Animator

    private var hasMouseOver = false

    private var isFocused = false

    private var animationLocation = 0f

    private var isAnimatedHint = true

    private var hasError = false

    init {

        border = EmptyBorder(20, 3, 10, 3)
        selectionColor = SemanticColor.PRIMARY

        addMouseListener(object : MouseAdapter() {

            override fun mouseEntered(me: MouseEvent?) {
                hasMouseOver = true
                repaint()
            }

            override fun mouseExited(me: MouseEvent?) {
                hasMouseOver = false
                repaint()
            }
        })

        addFocusListener(object : FocusAdapter() {
            override fun focusGained(fe: FocusEvent) {
                showing(false)
            }

            override fun focusLost(fe: FocusEvent) {
                showing(true)
            }
        })

        val target: TimingTarget = object : TimingTargetAdapter() {

            override fun begin() {
                isAnimatedHint = text.isEmpty()
            }

            override fun timingEvent(fraction: Float) {
                animationLocation = fraction
                repaint()
            }
        }

        animator = Animator(300, target)
        animator.resolution = 0
        animator.acceleration = 0.5f
        animator.deceleration = 0.5f

    }


    override fun paint(g: Graphics?) {
        super.paintComponent(g)
        val g2 = g as Graphics2D
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB)

        if (hasMouseOver) {
            g2.color = if (hasError) SemanticColor.DANGER else SemanticColor.PRIMARY
        } else {
            g2.color = SemanticColor.SECONDARY
        }

        g2.fillRect(2, height - 1, width - 4, 1)
        drawLabel(g2)
        drawLineStyle(g2)
        g2.dispose()
    }

    private fun showing(action: Boolean) {

        if (animator.isRunning) {
            animator.stop()
        } else {
            animationLocation = 1f
        }

        animationLocation = 1f - animationLocation
        animator.startFraction = animationLocation
        isFocused = action

        animator.start()
    }

    private fun drawLabel(g2: Graphics2D) {
        g2.color = SemanticColor.SECONDARY
        val fontMetrics = g2.fontMetrics
        val labelBounds = fontMetrics.getStringBounds(label, g2)
        val height = height - insets.top - insets.bottom
        val textY = (height - labelBounds.height) / 2

        val size = if (isAnimatedHint) {
            if (isFocused) 18 * (1 - animationLocation.toDouble())
            else 18 * animationLocation.toDouble()
        } else {
            18.0
        }

        g2.drawString(
            if (required) "$label*" else label,
            insets.right,
            (insets.top + textY + fontMetrics.ascent - size).toInt()
        )

    }

    private fun drawLineStyle(g2: Graphics2D) {
        if (isFocusOwner) {
            val width = (width - 4).toDouble()
            val height = height

            g2.color =
                if (hasError) SemanticColor.DANGER
                else SemanticColor.PRIMARY

            val size: Double =
                if (isFocused) width * (1 - animationLocation)
                else width * animationLocation

            val x = (width - size) / 2
            g2.fillRect((x + 2).toInt(), height - 2, size.toInt(), 2)
        }
    }

}
