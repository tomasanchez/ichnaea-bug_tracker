package org.ichnaea.controller

import org.ichnaea.core.mvc.controller.Controller
import org.ichnaea.core.ui.form.PasswordField
import org.ichnaea.core.ui.form.TextField
import org.slf4j.Logger
import java.awt.event.ActionEvent

@Controller
class SignUpController : BaseController() {

    private lateinit var usernameInput: TextField
    private lateinit var passwordInput: PasswordField
    private lateinit var confirmPasswordInput: PasswordField

    companion object {
        private val logger: Logger = org.slf4j.LoggerFactory.getLogger(SignUpController::class.java)
    }

    override fun onInit() {

        val signUpButton = this.byId("signUpButton") as org.ichnaea.core.ui.button.Button?
        signUpButton?.onClick(::onSignUp)

        val cancelButton = this.byId("cancelButton") as org.ichnaea.core.ui.button.Button?
        cancelButton?.onClick(::onCancel)

        usernameInput = this.byId("usernameField") as TextField
        passwordInput = this.byId("passwordField") as PasswordField
        confirmPasswordInput = this.byId("password2Field") as PasswordField

    }

    // ------------------------------
    // Event Handlers
    // ------------------------------


    private fun onSignUp(event: ActionEvent) {
        logger.info("Attempted SignUp")

        val hasError =
            validateEmpty(usernameInput.text, usernameInput)
                    ||
                    validateEmpty(passwordInput.value(), passwordInput)
                    ||
                    validateEmpty(confirmPasswordInput.value(), confirmPasswordInput)

        if (hasError)
            return

        doesPasswordMatch()


    }

    private fun onCancel(event: ActionEvent) {
        logger.info("Cancelled Sign Up. Navigating to Sign In View...")
        navTo("SignIn")
    }

    // ------------------------------
    // Internal Methods
    // ------------------------------

    private fun doesPasswordMatch(): Boolean {
        return (passwordInput.value() == confirmPasswordInput.value()).also {
            
            if (!it)
                logger.error("Passwords do not match")

            confirmPasswordInput.setError(!it)
            passwordInput.setError(!it)
        }
    }

}