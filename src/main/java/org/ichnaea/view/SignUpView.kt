package org.ichnaea.view

import org.ichnaea.core.mvc.view.View
import org.ichnaea.core.ui.button.Button
import org.ichnaea.core.ui.container.Toolbar
import org.ichnaea.core.ui.form.PasswordField
import org.ichnaea.core.ui.form.TextField
import org.ichnaea.core.ui.semantic.SemanticColor
import org.ichnaea.core.ui.text.Title
import org.ichnaea.core.ui.text.TitleLevel
import java.awt.Dimension
import javax.swing.Box

@View
class SignUpView : SideView() {

    init {
        registerForm()
    }

    private fun registerForm() {
        header()
        body()
        footer()
    }

    private fun header() {
        containerPanel.add(Box.createRigidArea(Dimension(0, 20)))
        containerPanel.add(Title(text = "Add a new User", level = TitleLevel.H2))
    }

    private fun body() {

        containerPanel.add(Box.createRigidArea(Dimension(0, 35)))
        set("usernameField", TextField(label = "Username"), containerPanel)
        containerPanel.add(Box.createRigidArea(Dimension(0, 35)))
        set("passwordField", PasswordField(), containerPanel)
        containerPanel.add(Box.createRigidArea(Dimension(0, 35)))
        set("password2Field", PasswordField(label = "Confirm Password"), containerPanel)

    }

    private fun footer() {
        containerPanel.add(Box.createRigidArea(Dimension(0, 35)))
        val footerPanel = Toolbar(color = SemanticColor.LIGHT)
        set("cancelButton", Button(text = "Reset", color = SemanticColor.SECONDARY), footerPanel)
        set("signUpButton", Button(text = "Add User"), footerPanel)
        panel.add(footerPanel, "align center, wrap")
    }


}