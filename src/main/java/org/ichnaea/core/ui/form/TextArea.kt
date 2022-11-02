package org.ichnaea.core.ui.form

import org.ichnaea.core.ui.container.ScrollContainer
import org.jdesktop.animation.timing.Animator
import java.awt.Dimension
import java.awt.event.FocusAdapter
import java.awt.event.FocusEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JTextArea
import javax.swing.border.EmptyBorder
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener


class TextArea(
    label: String,
    required: Boolean = false,
    var scroll: ScrollContainer = ScrollContainer(),
) : JTextArea(), Validatable {

    init {
        lineWrap = true
        wrapStyleWord = true
        border = EmptyBorder(0, 0, 0, 0)
        columns = 20
        rows = 5
        minimumSize = Dimension(300, 50)
        scroll.setViewportView(this)
        scroll.labelText = "$label${if (required) "*" else ""}"
        initComponents()
    }

    private fun initComponents() {
        listenFor()
    }

    private fun listenFor() {

        document.addDocumentListener(object : DocumentListener {
            override fun insertUpdate(e: DocumentEvent?) {
                scroll.isAnimateHinText = text == ""
            }

            override fun removeUpdate(e: DocumentEvent?) {
                scroll.isAnimateHinText = text == ""
            }

            override fun changedUpdate(e: DocumentEvent?) {
            }

        })

        addMouseListener(object : MouseAdapter() {
            override fun mouseEntered(me: MouseEvent?) {
                scroll.isMouseOver = true
            }

            override fun mouseExited(me: MouseEvent?) {
                scroll.isMouseOver = false
            }
        })

        addFocusListener(object : FocusAdapter() {
            override fun focusGained(fe: FocusEvent) {
                showing(false)
            }

            override fun focusLost(fe: FocusEvent) {
                showing(true)
            }
        })

    }

    private fun showing(action: Boolean) {
        val s: ScrollContainer = scroll

        val animator: Animator = s.animator
        var location: Float = s.animateLocation

        if (animator.isRunning) {
            animator.stop()
        } else {
            location = 1f
        }

        animator.startFraction = 1f - location
        s.isShow = action
        location = 1f - location
        s.animateLocation = location

        animator.start()
    }

    override fun setError(isValid: Boolean) {
        scroll.setError(isValid)
    }
}