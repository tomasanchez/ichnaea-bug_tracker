package org.ichnaea.core.ui.button

import jiconfont.IconCode
import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons
import org.ichnaea.core.ui.icon.GoogleIconFactory
import org.ichnaea.core.ui.semantic.SemanticColor
import java.awt.*


class IconButton(
    code: IconCode,
    background: Color = Color.WHITE,
    badges: Int? = null,
    color: Color = SemanticColor.DARK,
    size: Float = 30f,
) : SemanticButton() {


    init {

        isOpaque = true
        this.background = background
        hasIcon = true

        this.icon =
            GoogleIconFactory.build(
                name = code as GoogleMaterialDesignIcons,
                color = color,
                size = size
            )

        this.badges = badges
    }

    override fun paintComponent(grphcs: Graphics) {
        val g2 = grphcs as Graphics2D
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2.color = background
        g2.composite = AlphaComposite.SrcOver
        super.paintComponent(grphcs)
    }


}