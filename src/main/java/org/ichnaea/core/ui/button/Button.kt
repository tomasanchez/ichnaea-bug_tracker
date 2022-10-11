package org.ichnaea.core.ui.button

import org.ichnaea.core.ui.semantic.SemanticColor
import org.jdesktop.animation.timing.Animator
import org.jdesktop.animation.timing.TimingTarget
import org.jdesktop.animation.timing.TimingTargetAdapter
import java.awt.*
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.awt.image.BufferedImage
import javax.swing.JButton
import javax.swing.border.EmptyBorder


class Button(
    text: String,
    color: Color = SemanticColor.PRIMARY,
) : JButton() {


    private val animator: Animator
    private var targetSize = 0
    private var animateSize = 0f
    private var pressedPoint: Point? = null
    private var alpha = 0f


    init {
        this.text = text
        isContentAreaFilled = false
        border = EmptyBorder(8, 8, 8, 8)
        background = color
        foreground = when (color) {
            SemanticColor.PRIMARY,
            SemanticColor.DARK,
            SemanticColor.SECONDARY,
            SemanticColor.SUCCESS -> SemanticColor.LIGHT

            else -> SemanticColor.DARK
        }

        minimumSize = Dimension(50, 125)
        cursor = Cursor(Cursor.HAND_CURSOR)


        val target: TimingTarget = object : TimingTargetAdapter() {
            override fun timingEvent(fraction: Float) {
                if (fraction > 0.5f) {
                    alpha = 1 - fraction
                }
                animateSize = fraction * targetSize
                repaint()
            }
        }

        animator = Animator(500, target)
        animator.resolution = 0
        animator.acceleration = 0.5f
        animator.deceleration = 0.5f
        addMouseListener(object : MouseAdapter() {

            override fun mousePressed(me: MouseEvent) {
                targetSize = width.coerceAtLeast(height) * 2
                animateSize = 0f
                pressedPoint = me.point
                alpha = 0.5f
                if (animator.isRunning) {
                    animator.stop()
                }
                animator.start()
            }
        })

    }

    override fun paintComponent(grphcs: Graphics) {
        val width = width
        val height = height
        val img = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val g2 = img.createGraphics()

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2.color = background
        g2.fillRoundRect(0, 0, width, height, 10, 8)

        pressedPoint?.let {
            g2.color = SemanticColor.LIGHT
            g2.composite = AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha)
            g2.fillOval(
                (it.x - animateSize / 2).toInt(),
                (it.y - animateSize / 2).toInt(),
                animateSize.toInt(),
                animateSize.toInt()
            )
        }

        g2.dispose()
        grphcs.drawImage(img, 0, 0, null)
        super.paintComponent(grphcs)
    }
}