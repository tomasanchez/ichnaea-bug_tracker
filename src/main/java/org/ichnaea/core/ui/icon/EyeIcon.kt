package org.ichnaea.core.ui.icon

import java.awt.Image
import javax.swing.ImageIcon

class EyeIcon {

    private var eye: Image = ImageIcon(javaClass.getResource("/icon/eye.png")).image
    private var slashEye: Image = ImageIcon(javaClass.getResource("/icon/eye-slash.png")).image


    fun get(isVisible: Boolean): Image {
        return if (isVisible) {
            eye
        } else {
            slashEye
        }
    }

}