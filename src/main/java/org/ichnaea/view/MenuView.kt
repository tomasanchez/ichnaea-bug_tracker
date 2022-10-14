package org.ichnaea.view

import org.ichnaea.controller.BaseController.Companion.navTo
import org.ichnaea.core.ui.button.Button
import org.ichnaea.core.ui.container.Toolbar
import org.ichnaea.core.ui.semantic.SemanticColor
import java.awt.FlowLayout
import java.awt.event.ActionEvent
import javax.swing.BorderFactory
import javax.swing.BoxLayout

abstract class MenuView : BaseView() {

    companion object {
        val menu = Toolbar(alignment = FlowLayout.LEFT, color = SemanticColor.LIGHT)

        init {
            val signOut =
                Button(text = "Sign Out", color = SemanticColor.SECONDARY)
                    .also {
                        it.onClick(::onSignOut)
                    }

            menu.add(signOut)
        }

        private fun onSignOut(e: ActionEvent) = navTo("SignIn")
    }

    init {
        panel.layout = BoxLayout(panel, BoxLayout.PAGE_AXIS)
        panel.border = BorderFactory.createEmptyBorder(0, 0, 15, 0)
        createMenu()
    }

    private fun createMenu() {
        set(menu)
    }

}