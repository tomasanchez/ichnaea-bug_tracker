package org.ichnaea.core.ui.avatar

import org.ichnaea.core.ui.semantic.SemanticColor
import java.awt.*
import java.awt.geom.Area
import java.awt.geom.Ellipse2D
import java.awt.image.BufferedImage
import javax.swing.Icon
import javax.swing.ImageIcon
import javax.swing.JComponent


class Avatar(
    private val image: Icon,
    var color: Color = SemanticColor.LIGHT,
    private val borderSize: Int = 3,
    private val borderSpace: Int = 2,
    size: Dimension = AvatarSize.S
) : JComponent() {

    init {
        preferredSize = size
        minimumSize = AvatarSize.S.takeUnless { size == AvatarSize.XS } ?: size
        maximumSize = size
    }

    override fun paintComponent(grphcs: Graphics) {
        val g2 = grphcs as Graphics2D
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        createBorder(g2)

        val width = width
        val height = height
        val diameter: Int = width.coerceAtMost(height) - (borderSize * 2 + borderSpace * 2)
        val x = (width - diameter) / 2
        val y = (height - diameter) / 2
        val size: Rectangle = getAutoSize(image, diameter)

        val img = BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB)
        val g2Img = img.createGraphics()
        g2Img.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2Img.fillOval(0, 0, diameter, diameter)

        val composite: Composite = g2Img.composite
        g2Img.composite = AlphaComposite.SrcIn
        g2Img.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
        g2Img.drawImage(toImage(image), size.x, size.y, size.width, size.height, null)
        g2Img.composite = composite
        g2Img.dispose()
        g2.drawImage(img, x, y, null)

        super.paintComponent(grphcs)
    }

    private fun createBorder(g2: Graphics2D) {

        val width = width
        val height = height
        var diameter = width.coerceAtMost(height)

        val x = (width - diameter) / 2
        val y = (height - diameter) / 2

        if (isOpaque) {
            g2.color = background
            g2.fillOval(x, y, diameter, diameter)
        }

        val area = Area(Ellipse2D.Double(x.toDouble(), y.toDouble(), diameter.toDouble(), diameter.toDouble()))
        diameter -= borderSize * 2

        area.subtract(
            Area(
                Ellipse2D.Double(
                    (x + borderSize).toDouble(),
                    (y + borderSize).toDouble(),
                    diameter.toDouble(),
                    diameter.toDouble()
                )
            )
        )

        g2.paint = GradientPaint(0f, 0f, color, width.toFloat(), height.toFloat(), color)

        g2.fill(area)
    }

    private fun getAutoSize(image: Icon, size: Int): Rectangle {

        val iw = image.iconWidth
        val ih = image.iconHeight

        val xScale = size.toDouble() / iw
        val yScale = size.toDouble() / ih
        val scale = xScale.coerceAtLeast(yScale)

        var width = (scale * iw).toInt()
        var height = (scale * ih).toInt()

        if (width < 1) width = 1
        if (height < 1) height = 1

        val x = (size - width) / 2
        val y = (size - height) / 2

        return Rectangle(Point(x, y), Dimension(width, height))
    }

    private fun toImage(icon: Icon): Image = (icon as ImageIcon).image
}