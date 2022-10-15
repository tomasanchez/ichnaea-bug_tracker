package org.ichnaea.core.ui.button

import jiconfont.IconCode
import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons
import org.ichnaea.core.ui.icon.GoogleIconFactory
import org.ichnaea.core.ui.semantic.SemanticColor
import org.jdesktop.animation.timing.Animator
import java.awt.Color
import java.awt.Point
import java.awt.event.ActionEvent
import javax.swing.JButton

abstract class SemanticButton : JButton() {

    var isDarkIcon = true
    var hasIcon = false
    lateinit var animator: Animator
    var targetSize = 0
    var animateSize = 0f
    var pressedPoint: Point? = null
    var alpha = 0f

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

}