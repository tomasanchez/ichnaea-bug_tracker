package org.ichnaea.view

import net.miginfocom.swing.MigLayout
import org.ichnaea.core.mvc.view.UIView
import org.ichnaea.core.ui.avatar.Avatar
import org.ichnaea.core.ui.button.Button
import org.ichnaea.core.ui.form.PasswordField
import org.ichnaea.core.ui.form.TextField
import org.ichnaea.core.ui.semantic.SemanticColor
import org.ichnaea.core.ui.text.Title
import org.ichnaea.core.ui.text.TitleLevel
import java.awt.Dimension
import javax.swing.Box
import javax.swing.ImageIcon


@UIView
class SignInView : BaseView() {

    init {
        panel.layout = MigLayout("fill, insets 0")
        createLoginForm()
    }

    private fun createLoginForm() {
        header()
        body()
        footer()
    }

    private fun header() {
        val icon = ImageIcon(javaClass.getResource("/icon/ichnaea-icon.png"))
        set(Avatar(image = icon), "align center, wrap")
        set(
            "title",
            Title(text = "Ichnaea Issue Tracker", level = TitleLevel.H1, color = SemanticColor.PRIMARY),
            "align center, wrap"
        )
        set("subtitle", Title(text = "Sign In", level = TitleLevel.H2), "align center, wrap")
    }


    private fun body() {
        val usernameField = TextField(label = "Username")
        val passwordField = PasswordField(label = "Password")

        panel.add(Box.createRigidArea(Dimension(0, 45)), "wrap")
        set("usernameField", usernameField, "align center, wrap")
        panel.add(Box.createRigidArea(Dimension(0, 25)), "wrap")
        set("passwordField", passwordField, "align center, wrap")

    }

    private fun footer() {
        set(Box.createRigidArea(Dimension(0, 60)), "wrap")


        set(
            "signInButton",
            Button(text = "Sign In"),
            "align center, h 50!, w 200!, spanx"
        )

    }

}