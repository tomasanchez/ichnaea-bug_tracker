package org.ichnaea.view

import jiconfont.icons.google_material_design_icons.GoogleMaterialDesignIcons
import net.miginfocom.swing.MigLayout
import org.ichnaea.core.mvc.view.UIView
import org.ichnaea.core.ui.avatar.Avatar
import org.ichnaea.core.ui.icon.GoogleIconFactory
import org.ichnaea.core.ui.semantic.SemanticColor
import org.ichnaea.core.ui.text.Title
import org.ichnaea.core.ui.text.TitleLevel
import org.ichnaea.form.UserForm
import java.awt.Dimension
import javax.swing.Box


@UIView
class SignInView : BaseView() {

    private val userForm: UserForm = UserForm(submitButtonText = "Sign In")

    init {
        model["userForm"] = userForm
        panel.layout = MigLayout("fill, insets 0")
        initComponents()
    }

    private fun initComponents() {
        header()
        body()
        footer()
    }

    private fun header() {
        set(
            Avatar(
                GoogleIconFactory.build(
                    name = GoogleMaterialDesignIcons.TRACK_CHANGES,
                    color = SemanticColor.PRIMARY.darker()
                )
            ),
            "align center, wrap"
        )

        set(
            "title",
            Title(text = "Ichnaea Issue Tracker", level = TitleLevel.H1, color = SemanticColor.PRIMARY),
            "align center, wrap"
        )

        set("subtitle", Title(text = "Sign In", level = TitleLevel.H2), "align center, wrap")
    }


    private fun body() {

        panel.add(Box.createRigidArea(Dimension(0, 45)), "wrap")
        panel.add(userForm.username, "align center, wrap")
        panel.add(Box.createRigidArea(Dimension(0, 25)), "wrap")
        panel.add(userForm.password, "align center, wrap")

    }

    private fun footer() {
        set(Box.createRigidArea(Dimension(0, 60)), "wrap")


        panel.add(
            userForm.submitButton,
            "align center, h 50!, w 200!, spanx"
        )

    }

}