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
import javax.swing.BoxLayout

@View
class SignUpView : SideView() {

    init {
        panel.layout = BoxLayout(panel, BoxLayout.PAGE_AXIS)
        registerForm()
    }

    private fun registerForm() {
        header()
        body()
        footer()
    }

    private fun header() {
        set("subtitle", Title(text = "Add a new User", level = TitleLevel.H2))
    }

    private fun body() {

        set(Box.createRigidArea(Dimension(0, 35)))
        set("usernameField", TextField(label = "Username"))
        set(Box.createRigidArea(Dimension(0, 25)))
        set("passwordField", PasswordField())
        set(Box.createRigidArea(Dimension(0, 25)))
        set("password2Field", PasswordField(label = "Confirm Password"))

    }

    private fun footer() {
        set(Box.createRigidArea(Dimension(0, 35)))
        val footerPanel = Toolbar()
        set("cancelButton", Button(text = "Cancel", color = SemanticColor.DANGER), footerPanel)
        set("signUpButton", Button(text = "Sign Up"), footerPanel)
        set(footerPanel)
    }


}