package org.ichnaea.controller

import org.ichnaea.core.mvc.controller.Controller
import org.ichnaea.core.ui.button.Button
import org.ichnaea.core.ui.form.PasswordField
import org.ichnaea.core.ui.form.TextField
import org.slf4j.Logger
import java.awt.event.ActionEvent


@Controller
class SignInController : BaseController() {

    private lateinit var usernameInput: TextField
    private lateinit var passwordInput: PasswordField

    companion object {
        private val logger: Logger = org.slf4j.LoggerFactory.getLogger(SignInController::class.java)
    }

    override fun onInit() {

        val signInButton = this.byId("signInButton") as Button?
        signInButton?.onClick(::onSignIn)

        val signUpButton = this.byId("signUpButton") as Button?
        signUpButton?.onClick(::onSignUp)

        usernameInput = this.byId("usernameField") as TextField
        passwordInput = this.byId("passwordField") as PasswordField
    }

    // ------------------------------
    // Event Handlers
    // ------------------------------

    private fun onSignIn(event: ActionEvent) {

        val username = usernameInput.text
        val password = passwordInput.value()

        validateEmpty(username, usernameInput) || validateEmpty(password, passwordInput)

        logger.info("User '$username' attempted Sign In with '$password'")
    }

    private fun onSignUp(event: ActionEvent) {
        logger.info("Navigating to Sign Up View...")
        navTo("SignUp")
    }

    // ------------------------------
    // Internal Methods
    // ------------------------------


}


