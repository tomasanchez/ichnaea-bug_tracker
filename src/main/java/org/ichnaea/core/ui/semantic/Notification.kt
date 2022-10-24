package org.ichnaea.core.ui.semantic

import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons
import org.ichnaea.core.ui.button.IconButton
import org.ichnaea.core.ui.icon.GoogleIconFactory
import org.ichnaea.core.ui.text.Title
import org.ichnaea.core.ui.text.TitleLevel
import org.ichnaea.core.ui.text.Typography
import org.ichnaea.core.ui.utils.ShadowRenderer
import org.jdesktop.animation.timing.Animator
import org.jdesktop.animation.timing.TimingTarget
import org.jdesktop.animation.timing.TimingTargetAdapter
import java.awt.*
import java.awt.image.BufferedImage
import java.lang.Thread.sleep
import javax.swing.*


class Notification(
    private val app: JFrame,
    private val message: String,
    private val type: NotificationDataType = Type.DEFAULT,
    private val location: Location = Location.BOTTOM_CENTER,
) : JComponent() {

    private lateinit var dialog: JDialog
    private val closeButton = IconButton(GoogleMaterialDesignIcons.CLOSE, size = 16f).also { it.isOpaque = false }
    private val panel = JPanel()

    private lateinit var animator: Animator
    private var showing = false
    private var thread: Thread? = null
    private lateinit var imageShadow: BufferedImage
    private val shadowSize = 6
    private val animate = 10


    init {
        minimumSize = Dimension(375, 100)
        preferredSize = Dimension(375, 100)
        background = Color.WHITE
        closeButton.onClick { close() }
        initComponents()
        initAnimator()
    }


    private fun initComponents() {
        initDialog()
        initPanel()
    }

    private fun initDialog() {
        dialog = JDialog(app)
        dialog.isUndecorated = true
        dialog.focusableWindowState = false
        dialog.background = Color(0, 0, 0, 0)
        dialog.add(this)
        dialog.size = preferredSize
    }

    private fun initPanel() {
        val message = Typography(text = this.message, color = SemanticColor.SECONDARY)
        val title = Title(text = type.title, level = TitleLevel.H3, color = type.color)
        val icon = JLabel(type.icon)

        val panelLayout = GroupLayout(panel)
        panel.layout = panelLayout

        val maxValue: Int = Short.MAX_VALUE.toInt()

        panel.isOpaque = false
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    panelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(
                            panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(title)
                                .addComponent(message)
                        )
                        .addContainerGap(217, maxValue)
                )
        )

        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    panelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(title)
                        .addGap(3, 3, 3)
                        .addComponent(message)
                        .addContainerGap()
                )
        )

        val layout = GroupLayout(this)
        this.layout = layout
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(icon)
                        .addGap(10, 10, 10)
                        .addComponent(
                            panel,
                            GroupLayout.DEFAULT_SIZE,
                            GroupLayout.DEFAULT_SIZE,
                            maxValue
                        )
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(closeButton)
                        .addGap(15, 15, 15)
                )
        )

        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(
                            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(
                                    closeButton,
                                    GroupLayout.DEFAULT_SIZE,
                                    GroupLayout.DEFAULT_SIZE,
                                    maxValue
                                )
                                .addComponent(
                                    panel,
                                    GroupLayout.DEFAULT_SIZE,
                                    GroupLayout.DEFAULT_SIZE,
                                    maxValue
                                )
                                .addComponent(
                                    icon,
                                    GroupLayout.DEFAULT_SIZE,
                                    GroupLayout.DEFAULT_SIZE,
                                    maxValue
                                )
                        )
                        .addGap(10, 10, 10)
                )
        )

    }

    override fun paint(grphcs: Graphics) {
        val g2 = grphcs.create() as Graphics2D
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2.color = background
        g2.drawImage(imageShadow, 0, 0, null)
        val x = shadowSize
        val y = shadowSize
        val width = width - shadowSize * 2
        val height = height - shadowSize * 2
        g2.fillRect(x, y, width, height)
        g2.color = type.color
        g2.fillRect(6, 5, 7, height + 1)
        g2.dispose()
        super.paint(grphcs)
    }

    override fun setBounds(x: Int, y: Int, w: Int, h: Int) {
        super.setBounds(x, y, w, h)
        createImageShadow()
    }

    private fun createImageShadow() {
        imageShadow = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val g2 = imageShadow.createGraphics()
        g2.drawImage(createShadow(), 0, 0, null)
        g2.dispose()
    }

    private fun createShadow(): BufferedImage? {
        val img = BufferedImage(width - shadowSize * 2, height - shadowSize * 2, BufferedImage.TYPE_INT_ARGB)
        val g2 = img.createGraphics()
        g2.fillRect(0, 0, img.width, img.height)
        g2.dispose()
        return ShadowRenderer(shadowSize, 0.2f, SemanticColor.DARK.brighter()).createShadow(img)
    }

    private fun initAnimator() {
        val target: TimingTarget = object : TimingTargetAdapter() {
            private var x = 0
            private var top = 0
            private var top_to_bot = false

            override fun timingEvent(fraction: Float) {
                if (showing) {
                    val alpha = 1f - fraction
                    val y = ((1f - fraction) * animate).toInt()
                    if (top_to_bot) {
                        dialog.setLocation(x, top + y)
                    } else {
                        dialog.setLocation(x, top - y)
                    }
                    dialog.opacity = alpha
                } else {
                    val y = (fraction * animate).toInt()
                    if (top_to_bot) {
                        dialog.setLocation(x, top + y)
                    } else {
                        dialog.setLocation(x, top - y)
                    }
                    dialog.opacity = fraction
                }
            }

            override fun begin() {

                if (showing)
                    return
                dialog.opacity = 0f

                val margin = 10
                val y: Int

                when (location) {

                    Location.TOP_CENTER -> {
                        x = app.x + (app.width - dialog.width) / 2
                        y = app.y
                        top_to_bot = true
                    }

                    Location.TOP_LEFT -> {
                        x = app.x + margin
                        y = app.y
                        top_to_bot = true
                    }

                    Location.TOP_RIGHT -> {
                        x = app.x + app.width - dialog.width - margin
                        y = app.y
                        top_to_bot = true
                    }

                    Location.BOTTOM_CENTER -> {
                        x = app.x + (app.width - dialog.width) / 2
                        y = app.y + app.height - dialog.height
                        top_to_bot = false
                    }

                    Location.BOTTOM_LEFT -> {
                        x = app.x + margin
                        y = app.y + app.height - dialog.height
                        top_to_bot = false
                    }

                    Location.BOTTOM_RIGHT -> {
                        x = app.x + app.width - dialog.width - margin
                        y = app.y + app.height - dialog.height
                        top_to_bot = false
                    }

                    else -> {
                        x = app.x + (app.width - dialog.width) / 2
                        y = app.y + (app.height - dialog.height) / 2
                        top_to_bot = true
                    }
                }

                top = y
                dialog.setLocation(x, y)
                dialog.isVisible = true
            }

            override fun end() {

                showing = !showing

                if (!showing) {
                    dialog.dispose()
                    return
                }

                thread = Thread {
                    forceSleep()
                    close()
                }

                thread?.start()

            }
        }

        animator = Animator(500, target)
        animator.resolution = 5
    }


    data class NotificationDataType(
        val title: String,
        val color: Color,
        val icon: Icon,
    )

    class Type {
        companion object {

            val SUCCESS = NotificationDataType(
                "Success",
                SemanticColor.SUCCESS,
                GoogleIconFactory.build(
                    name = GoogleMaterialDesignIcons.CHECK,
                    color = SemanticColor.SUCCESS,
                    size = 24f,
                )
            )

            val ERROR = NotificationDataType(
                "Error",
                SemanticColor.DANGER,
                GoogleIconFactory.build(
                    name = GoogleMaterialDesignIcons.ERROR,
                    color = SemanticColor.DANGER,
                    size = 24f
                )
            )

            val WARNING = NotificationDataType(
                "Warning",
                SemanticColor.WARNING,
                GoogleIconFactory.build(
                    name = GoogleMaterialDesignIcons.WARNING,
                    color = SemanticColor.WARNING,
                    size = 24f
                )
            )

            val INFO = NotificationDataType(
                "Info",
                SemanticColor.INFO,
                GoogleIconFactory.build(
                    name = GoogleMaterialDesignIcons.INFO,
                    color = SemanticColor.INFO,
                    size = 24f
                )
            )

            val DEFAULT = NotificationDataType(
                "Info",
                SemanticColor.PRIMARY,
                GoogleIconFactory.build(
                    name = GoogleMaterialDesignIcons.REPORT,
                    color = SemanticColor.PRIMARY,
                    size = 24f
                )
            )
        }
    }

    fun pop() {
        animator.start()
    }

    private fun forceSleep() {
        try {
            sleep(3000)
        } catch (_: InterruptedException) {
        }
    }

    private fun close() {

        thread?.let {

            if (it.isAlive)
                it.interrupt()

        }


        if (animator.isRunning) {
            if (!showing) {
                animator.stop()
                showing = true
                animator.start()
            }
        } else {
            showing = true
            animator.start()
        }

    }

    enum class Location {
        TOP_CENTER, TOP_RIGHT, TOP_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT, BOTTOM_LEFT, CENTER
    }


}