package org.ichnaea.core.ui.avatar

import org.ichnaea.core.ui.semantic.SemanticColor
import java.awt.Dimension
import java.awt.Font
import java.awt.Image
import javax.swing.GroupLayout
import javax.swing.ImageIcon
import javax.swing.JLabel
import javax.swing.JPanel

class BrandLogo(
    text: String,
    imagePath: String?,
) : JPanel() {

    private val logo = JLabel()

    init {
        initComponents(text, imagePath)
        isOpaque = true
        background = SemanticColor.WARNING
        maximumSize = Dimension(Short.MAX_VALUE.toInt(), 100)
    }

    private fun initComponents(logoText: String, imagePath: String?) {
        logo.font = font.deriveFont(Font.BOLD, 16f)
        logo.foreground = SemanticColor.DARK.darker()
        val image = imagePath?.let { ImageIcon(javaClass.getResource(it)) }?.image
        val newImage = image?.getScaledInstance(45, 45, Image.SCALE_SMOOTH)
        logo.icon = newImage?.let { ImageIcon(it) }
        logo.text = logoText
        logo.maximumSize = Dimension(100, 100)

        val layout = GroupLayout(this)

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(logo, GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE.toInt())
                        .addGap(10, 10, 10)
                )
        )

        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(logo, GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE.toInt())
                        .addContainerGap()
                )
        )
    }
}