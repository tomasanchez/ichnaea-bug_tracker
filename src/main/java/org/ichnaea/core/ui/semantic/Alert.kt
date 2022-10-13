package org.ichnaea.core.ui.semantic

import jiconfont.IconCode
import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons
import jiconfont.swing.IconFontSwing
import org.ichnaea.core.ui.text.Title
import org.ichnaea.core.ui.text.TitleLevel
import org.ichnaea.core.ui.text.Typography
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.RenderingHints
import javax.swing.GroupLayout
import javax.swing.JPanel
import javax.swing.LayoutStyle
import javax.swing.border.LineBorder


class Alert(
    title: String = "",
    message: String,
    color: Color = SemanticColor.PRIMARY,
    showIcon: Boolean = true,
    icon: IconCode? = null,
) : JPanel() {

    init {

        when (color) {

            SemanticColor.PRIMARY -> {
                this.background = Color.decode("#cfe2ff")
                this.border = LineBorder(Color.decode("#b6d4fe"), 2)
            }

            SemanticColor.SECONDARY -> {
                this.background = Color.decode("#e2e3e5")
                this.border = LineBorder(Color.decode("#d6d8db"), 2)
            }

            SemanticColor.SUCCESS -> {
                this.background = Color.decode("#d1e7dd")
                this.border = LineBorder(Color.decode("#badbcc"), 2)
            }

            SemanticColor.DANGER -> {
                this.background = Color.decode("#f8d7da")
                this.border = LineBorder(Color.decode("#f5c2c7"), 2)
            }

            SemanticColor.WARNING -> {
                this.background = Color.decode("#fff3cd")
                this.border = LineBorder(Color.decode("#ffeeba"), 2)
            }

            SemanticColor.INFO -> {
                this.background = Color.decode("#cff4fc")
                this.border = LineBorder(Color.decode("#bee5eb"), 2)
            }

            SemanticColor.LIGHT -> {
                this.background = Color.decode("#fefefe")
                this.border = LineBorder(Color.decode("#fdfdfe"), 2)
            }

            SemanticColor.DARK -> {
                this.background = Color.decode("#d6d8d9")
                this.border = LineBorder(Color.decode("#c6c8ca"), 2)
            }

        }

        val groupLayout = GroupLayout(this)

        layout = groupLayout

        val alertTitle = Title(
            text = title,
            color = color,
            level = TitleLevel.H2
        )

        if (showIcon && title.isNotEmpty()) {
            icon?.let { addTitleIcon(alertTitle, it, color) } ?: addTitleIcon(alertTitle, color)
        }


        val alertMessage = Typography(
            text = message,
            color = color,
        )

        groupLayout.setHorizontalGroup(
            groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    groupLayout.createSequentialGroup()
                        .addGap(10, 15, 25)
                        .addGroup(
                            groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(alertMessage)
                                .addComponent(alertTitle)
                        )
                        .addContainerGap(50, Short.MAX_VALUE.toInt())
                )
        )

        groupLayout.setVerticalGroup(
            groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    groupLayout.createSequentialGroup()
                        .addGap(10, 10, 25)
                        .addComponent(alertTitle)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(alertMessage)
                        .addContainerGap(15, Short.MAX_VALUE.toInt())
                )
        )

    }

    override fun paintComponent(grphcs: Graphics) {
        val g2 = grphcs as Graphics2D
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2.fillRoundRect(0, 0, width, height, 15, 15)
        super.paintComponent(grphcs)
    }

    private fun addTitleIcon(title: Title, color: Color) {
        IconFontSwing.register(GoogleMaterialDesignIcons.getIconFont())

        val icon = IconFontSwing.buildIcon(
            when (color) {
                SemanticColor.PRIMARY -> GoogleMaterialDesignIcons.HELP
                SemanticColor.SECONDARY -> GoogleMaterialDesignIcons.DONE
                SemanticColor.INFO -> GoogleMaterialDesignIcons.INFO
                SemanticColor.SUCCESS -> GoogleMaterialDesignIcons.CHECK
                SemanticColor.WARNING -> GoogleMaterialDesignIcons.WARNING
                SemanticColor.DANGER -> GoogleMaterialDesignIcons.ERROR
                else -> GoogleMaterialDesignIcons.INFO
            },
            20f,
            color
        )


        title.icon = icon
    }

    private fun addTitleIcon(title: Title, icon: IconCode, color: Color) {
        IconFontSwing.register(GoogleMaterialDesignIcons.getIconFont())
        title.icon = IconFontSwing.buildIcon(icon, 30f, color)
    }


}