package org.ichnaea.controller

import org.ichnaea.core.mvc.controller.UIController
import org.ichnaea.core.ui.button.Button
import org.ichnaea.core.ui.form.PasswordField
import org.ichnaea.core.ui.form.TextField
import org.ichnaea.core.ui.semantic.SemanticColor
import org.slf4j.Logger
import java.awt.event.ActionEvent


@UIController
class SignInController : BaseController() {

    private lateinit var usernameInput: TextField
    private lateinit var passwordInput: PasswordField

    companion object {
        private val logger: Logger = org.slf4j.LoggerFactory.getLogger(SignInController::class.java)
    }

    // -------------------------------------------------------------
    // Lifecycle Methods
    // -------------------------------------------------------------

    override fun onInit() {

        val signInButton = this.byId("signInButton") as Button?
        signInButton?.onClick(::onSignIn)

        usernameInput = this.byId("usernameField") as TextField
        passwordInput = this.byId("passwordField") as PasswordField
    }

    override fun onBeforeRendering() {
    }

    override fun onAfterRendering() {

    }

    // ------------------------------
    // Event Handlers
    // ------------------------------

    private fun onSignIn(event: ActionEvent) {

        val username = usernameInput.text
        val password = passwordInput.value()

        val anyEmptyFields = validateEmpty(username, usernameInput) || validateEmpty(password, passwordInput)

        logger.info("Sign In attempted")

        if (anyEmptyFields) {
            showAlert(
                title = "Error",
                message = "Please fill in all fields",
                color = SemanticColor.DANGER
            )
            logger.error("Sign in failed: empty fields")
            return
        }

        logger.info("$username has Signed in successful")

        showAlert(
            title = "Hello, $username!",
            message = "You are being redirected to the home page...",
            color = SemanticColor.SUCCESS
        )

        clearInputs()
        navTo("Projects")

    }

    // ------------------------------
    // Internal Methods
    // ------------------------------

    private fun clearInputs() {
        usernameInput.text = ""
        passwordInput.text = ""

        usernameInput.setError(false)
        passwordInput.setError(false)
    }

}


