package org.ichnaea.view

import org.ichnaea.core.mvc.view.View
import org.ichnaea.core.ui.avatar.Avatar
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
import javax.swing.ImageIcon


@View
class SignInView : BaseView() {

    init {
        createLoginForm()
    }

    private fun createLoginForm() {
        header()
        body()
        footer()
    }

    private fun header() {
        val icon = ImageIcon(javaClass.getResource("/icon/ichnaea-icon.png"))
        set(Avatar(image = icon))
        set("title", Title(text = "Ichnaea Issue Tracker", level = TitleLevel.H1, color = SemanticColor.PRIMARY))
        set("subtitle", Title(text = "Sign In", level = TitleLevel.H2))
        panel.layout = BoxLayout(panel, BoxLayout.PAGE_AXIS)
    }


    private fun body() {
        val usernameField = TextField(label = "Username")
        val passwordField = PasswordField(label = "Password")

        panel.add(Box.createRigidArea(Dimension(0, 45)))
        set("usernameField", usernameField)
        panel.add(Box.createRigidArea(Dimension(0, 25)))
        set("passwordField", passwordField)

    }

    private fun footer() {
        set(Box.createRigidArea(Dimension(0, 60)))

        val footerPanel = Toolbar()
        set("signUpButton", Button(text = "Sign Up", color = SemanticColor.SECONDARY), footerPanel)
        set("signInButton", Button(text = "Sign In"), footerPanel)

        set(footerPanel)

    }

}