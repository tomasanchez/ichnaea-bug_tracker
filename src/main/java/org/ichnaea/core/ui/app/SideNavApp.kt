package org.ichnaea.core.ui.app

import net.miginfocom.swing.MigLayout
import org.ichnaea.core.ui.navigation.AppBar
import org.ichnaea.core.ui.navigation.SideNav
import org.jdesktop.animation.timing.Animator
import org.jdesktop.animation.timing.TimingTarget
import org.jdesktop.animation.timing.TimingTargetAdapter
import java.awt.Color
import java.awt.Dimension
import java.net.URL
import javax.swing.BorderFactory
import javax.swing.ImageIcon
import javax.swing.JPanel


class SideNavApp(
    title: String,
    iconResourcePath: String?,
) : AppUI() {

    companion object {
        const val DEFAULT_WIDTH = 800
        const val DEFAULT_HEIGHT = 600
        const val SIDE_NAV_MAX_WIDTH = 180
        const val SIDE_NAV_MIN_WIDTH = 100
    }

    private val animator: Animator

    private var body = JPanel()

    var sideNav = SideNav()

    var appBar = AppBar()

    init {
        val defaultSize = Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT)
        this.title = title
        defaultCloseOperation = EXIT_ON_CLOSE
        size = defaultSize
        preferredSize = defaultSize
        setLocationRelativeTo(null)
        iconResourcePath?.let { setIcon(it) }
        initComponents()

        val target: TimingTarget = object : TimingTargetAdapter() {
            override fun timingEvent(fraction: Float) {

                val maxWidth = SIDE_NAV_MAX_WIDTH
                val minWidth = SIDE_NAV_MIN_WIDTH

                val width: Double = if (sideNav.isShown) {
                    (minWidth + (maxWidth - minWidth) * (1f - fraction)).toDouble()
                } else {
                    (minWidth + (maxWidth - minWidth) * fraction).toDouble()
                }
                
                (contentPane.layout as MigLayout).setComponentConstraints(sideNav, "w $width!, spany2")
                sideNav.revalidate()
            }

            override fun end() {
                sideNav.isShown = !sideNav.isShown
                sideNav.isNavEnabled = true
            }
        }

        animator = Animator(500, target)
        animator.resolution = 0;
        animator.deceleration = 0.5f;
        animator.acceleration = 0.5f;

        appBar.onBars {

            if (!animator.isRunning) {
                animator.start()
            }

            sideNav.isNavEnabled = false

            if (sideNav.isShown) {
                sideNav.hideNav()
            }

        }

        pack()
        isVisible = true
    }

    private fun initComponents() {
        val layout = defaultLayout()
        val bg = JPanel()
        bg.border = BorderFactory.createEmptyBorder(0, 0, 0, 0)
        bg.background = Color.RED
        bg.layout = layout
        bg.add(sideNav, "w $SIDE_NAV_MAX_WIDTH!, spany 2")
        bg.add(appBar, "h 50!, wrap")
        bg.add(body, "w 100%, h 100%")
        contentPane = bg
    }

    private fun setIcon(resourcePath: String) {
        val location: URL? = javaClass.getResource(resourcePath)
        location?.let {
            val image = ImageIcon(it)
            iconImage = image.image
        }
    }


    override fun getBody(): JPanel {
        return this.body
    }

    override fun setBody(body: JPanel, isFullScreen: Boolean) {
        this.body = body
        reAdjust(isFullScreen)
    }

    override fun showBody(show: Boolean) {
        getBody().isVisible = show
    }


    private fun fullScreenLayout(): MigLayout {
        return MigLayout("fill")
    }

    private fun defaultLayout(): MigLayout {
        return MigLayout(
            "fill",
            "5[]5[100%, fill]5",
            "5[fill, top]5"
        )
    }

    private fun reAdjust(isFullScreen: Boolean) {
        val bg = contentPane
        bg.removeAll()

        if (isFullScreen) {
            bg.layout = fullScreenLayout()
            bg.add(body, "w 100%, h 100%")
        } else {
            initComponents()
        }

        repaint()
        revalidate()
        pack()
    }

}