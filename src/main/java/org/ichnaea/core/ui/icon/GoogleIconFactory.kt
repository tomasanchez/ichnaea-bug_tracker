package org.ichnaea.core.ui.icon

import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons
import jiconfont.swing.IconFontSwing
import org.ichnaea.core.ui.semantic.SemanticColor
import java.awt.Color
import javax.swing.Icon

object GoogleIconFactory {

    fun build(
        name: GoogleMaterialDesignIcons,
        color: Color = SemanticColor.PRIMARY,
        size: Float = 100f
    ): Icon {
        IconFontSwing.register(GoogleMaterialDesignIcons.getIconFont())
        return IconFontSwing.buildIcon(name, size, color)
    }

}