package org.ichnaea.core.ui.navigation

import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons
import org.ichnaea.core.ui.avatar.Avatar
import org.ichnaea.core.ui.avatar.AvatarSize
import org.ichnaea.core.ui.button.IconButton
import org.ichnaea.core.ui.button.SemanticButton
import org.ichnaea.core.ui.icon.GoogleIconFactory
import org.ichnaea.core.ui.semantic.SemanticColor
import org.ichnaea.core.ui.text.Title
import org.ichnaea.core.ui.text.TitleLevel
import org.ichnaea.core.ui.text.Typography
import java.awt.Color
import java.awt.FlowLayout
import javax.swing.GroupLayout
import javax.swing.JPanel
import javax.swing.JSeparator
import javax.swing.border.EmptyBorder

class AppBar : JPanel() {

    private lateinit var bars: SemanticButton

    init {
        border = EmptyBorder(0, 0, 0, 0)
        isOpaque = true
        background = Color.WHITE
        layout = FlowLayout(FlowLayout.LEFT, 0, 0)
        initComponents()
    }

    fun onBars(action: () -> Unit) = bars.onClick { action() }

    // --------------------------------
    // Components
    // --------------------------------

    private fun initComponents() {

        bars = IconButton(
            code = GoogleMaterialDesignIcons.MENU,
            color = SemanticColor.SECONDARY,
        )

        val pic = Avatar(
            image = GoogleIconFactory.build(
                name = GoogleMaterialDesignIcons.ACCOUNT_CIRCLE,
                color = SemanticColor.SECONDARY.darker(),
            ),
            color = SemanticColor.LIGHT,
            size = AvatarSize.XS,
        )

        val userName = Title(
            text = "Peter",
            level = TitleLevel.H3,
        )

        val role = Typography(
            text = "Admin",
        )

        val separator = JSeparator()
        separator.orientation = JSeparator.VERTICAL
        separator.background = SemanticColor.SECONDARY


        val layout = GroupLayout(this)

        val maxValue = Short.MAX_VALUE.toInt()

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(bars, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 362, maxValue)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(
                            separator,
                            GroupLayout.PREFERRED_SIZE,
                            8,
                            GroupLayout.PREFERRED_SIZE
                        )
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(
                            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(userName, GroupLayout.Alignment.TRAILING)
                                .addComponent(role, GroupLayout.Alignment.TRAILING)
                        )
                        .addGap(18, 18, 18)
                        .addComponent(
                            pic,
                            GroupLayout.PREFERRED_SIZE,
                            38,
                            GroupLayout.PREFERRED_SIZE
                        )
                        .addContainerGap()
                )
        )

        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(
                                    layout.createSequentialGroup()
                                        .addComponent(userName)
                                        .addPreferredGap(
                                            javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                            GroupLayout.DEFAULT_SIZE,
                                            maxValue
                                        )
                                        .addComponent(role)
                                )
                                .addComponent(
                                    bars,
                                    GroupLayout.DEFAULT_SIZE,
                                    GroupLayout.DEFAULT_SIZE,
                                    maxValue
                                )
                                .addComponent(
                                    pic,
                                    GroupLayout.DEFAULT_SIZE,
                                    GroupLayout.DEFAULT_SIZE,
                                    maxValue
                                )
                                .addComponent(separator)
                        )
                        .addContainerGap()
                )
        )

        this.layout = layout
    }

    fun onMenu() {
        println("onMenu")
    }

}