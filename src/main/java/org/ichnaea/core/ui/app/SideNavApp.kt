package org.ichnaea.core.ui.app

import java.awt.Dimension
import java.net.URL
import javax.swing.ImageIcon
import javax.swing.JPanel

class SideNavApp(
    title: String,
    iconResourcePath: String?,
) : AppUI() {

    companion object {
        const val DEFAULT_WIDTH = 720
        const val DEFAULT_HEIGHT = 600
    }

    private var body = JPanel()

    init {
        val defaultSize = Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT)

        this.title = title
        defaultCloseOperation = EXIT_ON_CLOSE
        size = defaultSize
        preferredSize = defaultSize
        setLocationRelativeTo(null)

        iconResourcePath?.let { setIcon(it) }
        pack()
        isVisible = true
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

    override fun setBody(body: JPanel) {
        this.body = body
        revalidate()
        repaint()
        pack()
    }

    override fun showBody(show: Boolean) {
        getBody().isVisible = show
    }


}