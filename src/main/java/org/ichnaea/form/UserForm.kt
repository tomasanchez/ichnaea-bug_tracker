package org.ichnaea.form

import org.ichnaea.core.ui.button.Button
import org.ichnaea.core.ui.form.PasswordField
import org.ichnaea.core.ui.form.TextField
import org.ichnaea.core.ui.semantic.SemanticColor
import org.ichnaea.model.User

class UserForm(
    val user: User? = null,
    submitButtonText: String = "Submit",
    cancelButtonText: String = "Cancel",
) : Form() {

    val username: TextField = TextField(label = "Username").also { it.isOpaque = false }

    val password: PasswordField = PasswordField(label = "Password").also { it.isOpaque = false }

    val confirmPassword: PasswordField = PasswordField(label = "Confirm Password").also { it.isOpaque = false }

    val submitButton = Button(text = submitButtonText)

    val cancelButton = Button(text = cancelButtonText, color = SemanticColor.SECONDARY)

    init {
        username.text = user?.userName
    }

}
