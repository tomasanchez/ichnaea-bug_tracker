package org.ichnaea.core.ui.form

import org.ichnaea.core.ui.icon.EyeIcon
import org.ichnaea.core.ui.semantic.SemanticColor
import org.jdesktop.animation.timing.Animator
import org.jdesktop.animation.timing.TimingTarget
import org.jdesktop.animation.timing.TimingTargetAdapter
import java.awt.*
import java.awt.event.*
import javax.swing.JPasswordField
import javax.swing.border.EmptyBorder


class PasswordField(
    val label: String = "Password",
) : JPasswordField(), Validatable {

    private var hasError = false

    private val animator: Animator

    private var hasMouseOver = false

    private val icon = EyeIcon()

    private var isShown = false

    private var isAnimatedHint = true

    private var isShowAndHide = true

    private var animationLocation = 0f

    private var isFocused = false

    init {
        border = EmptyBorder(20, 3, 10, 30)
        selectionColor = SemanticColor.PRIMARY

        minimumSize = Dimension(300, 45)
        maximumSize = minimumSize


        addMouseListener(object : MouseAdapter() {

            override fun mouseEntered(me: MouseEvent?) {
                hasMouseOver = true
                repaint()
            }

            override fun mouseExited(me: MouseEvent?) {
                hasMouseOver = false
                repaint()
            }

            override fun mousePressed(me: MouseEvent) {
                if (isShowAndHide) {
                    val x = width - 30
                    if (Rectangle(x, 0, 30, 30).contains(me.point)) {
                        isShown = !isShown
                        echoChar =
                            if (!isShown) '•'
                            else 0.toChar()

                        repaint()
                    }
                }
            }
        })

        addMouseMotionListener(object : MouseMotionAdapter() {
            override fun mouseMoved(me: MouseEvent) {
                if (isShowAndHide) {
                    val x = width - 30
                    cursor = if (Rectangle(x, 0, 30, 30).contains(me.point)) {
                        Cursor(Cursor.HAND_CURSOR)
                    } else {
                        Cursor(Cursor.TEXT_CURSOR)
                    }
                }
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
                isAnimatedHint = value().isEmpty()
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

    override fun paint(grphcs: Graphics) {
        super.paint(grphcs)
        val g2 = grphcs as Graphics2D

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB)

        val width = width
        val height = height

        g2.color =
            if (hasError) SemanticColor.DANGER
            else
                if (!hasMouseOver) SemanticColor.SECONDARY
                else
                    SemanticColor.PRIMARY

        g2.fillRect(2, height - 1, width - 4, 1)
        drawLabel(g2)
        drawLineStyle(g2)

        if (isShowAndHide)
            drawShowAndHide(g2)

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

    private fun drawShowAndHide(g2: Graphics2D) {
        val x = width - 30 + 5
        val y = (height - 20) / 2
        g2.drawImage(icon.get(isShown), x, y, null)
    }

    private fun drawLabel(g2: Graphics2D) {

        val fontMetrics = g2.fontMetrics

        g2.color = if (hasError) SemanticColor.DANGER else SemanticColor.SECONDARY

        val labelBounds = fontMetrics.getStringBounds(label, g2)

        val height = height - insets.top - insets.bottom


        val size = if (isAnimatedHint) {
            if (isFocused) 18 * (1 - animationLocation.toDouble())
            else 18 * animationLocation.toDouble()
        } else {
            18.0
        }
        val textY = (height - labelBounds.height) / 2

        g2.drawString(
            "$label*",
            insets.right,
            (insets.top + textY + fontMetrics.ascent - size).toInt()
        )

    }

    private fun drawLineStyle(g2: Graphics2D) {

        g2.color = if (hasError) SemanticColor.DANGER else SemanticColor.SECONDARY

        if (isFocusOwner) {
            g2.color = if (hasError) SemanticColor.DANGER else SemanticColor.PRIMARY

            val width = (width - 4).toDouble()

            val size: Double =
                if (isFocused) width * (1 - animationLocation)
                else width * animationLocation

            val x = (width - size) / 2
            g2.fillRect((x + 2).toInt(), height - 2, size.toInt(), 2)
        }
    }

    override fun setText(string: String) {
        if (java.lang.String.valueOf(password) != string) {
            showing(string.isEmpty())
        }
        super.setText(string)
    }

    override fun setError(isValid: Boolean) {
        this.hasError = isValid
        repaint()
    }

    fun value(): String {
        return try {
            String(password)
        } catch (NPE: NullPointerException) {
            String()
        }
    }

}