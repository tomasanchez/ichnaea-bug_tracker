package org.ichnaea.core.ui.button

import jiconfont.IconCode
import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons
import org.ichnaea.core.ui.icon.GoogleIconFactory
import org.ichnaea.core.ui.semantic.SemanticColor
import org.jdesktop.animation.timing.Animator
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.geom.Rectangle2D
import javax.swing.JButton


abstract class SemanticButton : JButton() {

    var isDarkIcon = true
    var hasIcon = false
    lateinit var animator: Animator
    var targetSize = 0
    var animateSize = 0f
    var pressedPoint: Point? = null
    var alpha = 0f
    var badges: Int? = null

    init {
        border = null
        isContentAreaFilled = false
        isFocusPainted = false
    }

    /**
     * Set semantic color
     *
     * @param color the button color
     */
    fun setSemanticColor(color: Color) {
        background = color
        foreground = when (color) {
            SemanticColor.PRIMARY,
            SemanticColor.DARK,
            SemanticColor.DANGER,
            SemanticColor.SECONDARY,
            SemanticColor.SUCCESS,
            -> SemanticColor.LIGHT.also { isDarkIcon = false }

            else -> SemanticColor.DARK
        }
    }

    /**
     * Creates a Google Material Icon for the button.
     *
     * @param icon the icon code name
     */
    fun createIcon(icon: IconCode?) {
        icon?.let {
            if (it in GoogleMaterialDesignIcons.values()) {
                this.icon =
                    GoogleIconFactory.build(
                        name = it as GoogleMaterialDesignIcons,
                        color = if (isDarkIcon) SemanticColor.DARK else SemanticColor.LIGHT,
                        size = this.font.size.toFloat() + 15f
                    ).also { hasIcon = true }
            }
        }
    }

    /**
     * Event handler for button click
     *
     * @param action the event handler
     */
    fun onClick(action: (e: ActionEvent) -> Unit) {
        addActionListener(action)
    }

    override fun paint(g: Graphics) {
        super.paint(g)
        badges?.let {
            if (it > 0) {

                val value = if (it > 99) "99+" else it.toString()
                val g2 = g.create() as Graphics2D

                val ft = g2.fontMetrics
                val r2: Rectangle2D = ft.getStringBounds(value, g2)
                val fw = r2.width
                g2.color = SemanticColor.DANGER
                val size = (fw.coerceAtLeast(r2.height)).toInt()
                g2.composite = AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 1f)
                g2.fillOval(width - size, 0, size, size)
                val x = (size - fw) / 2
                g2.color = Color.WHITE
                g2.composite = AlphaComposite.SrcOver
                g2.drawString(value, (width - size + x).toFloat(), (ft.ascent + 1).toFloat())
                g2.dispose()
            }
        }
    }

}