package org.ichnaea.controller

import org.ichnaea.auth.IchnaeaSessionProvider
import org.ichnaea.core.exception.AuthenticationException
import org.ichnaea.core.mvc.controller.UIController
import org.ichnaea.core.security.auth.Authentication
import org.ichnaea.core.ui.button.Button
import org.ichnaea.core.ui.form.PasswordField
import org.ichnaea.core.ui.form.TextField
import org.ichnaea.core.ui.semantic.SemanticColor
import org.ichnaea.service.UserService
import org.slf4j.Logger
import java.awt.event.ActionEvent


@UIController
class SignInController : BaseController() {

    private lateinit var usernameInput: TextField
    private lateinit var passwordInput: PasswordField
    private val userService = UserService()
    private val sessionProvider: IchnaeaSessionProvider =
        getSecurityContext().sessionManager as IchnaeaSessionProvider


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

        sessionProvider.attemptAuthentication(username, password, this::onSuccessfulSignIn, this::onFailedSignIn)
            ?: run {
                logger.error("Failed to sign in: session provider is null")
            }

    }

    // ------------------------------
    // Internal Methods
    // ------------------------------

    private fun onSuccessfulSignIn(auth: Authentication) {
        logger.info("Sign in successful for ${auth.name}")

        val user = userService.findByUsername(auth.name)

        sessionProvider.session = user.get()
        clearInputs()
        navTo("Projects")
    }

    private fun onFailedSignIn(e: AuthenticationException) {

        logger.error("Sign in failed")

        usernameInput.setError(true)
        passwordInput.setError(true)
        passwordInput.text = ""

        showAlert(
            title = "Error",
            message = "Sign in failed: ${e.message}",
            color = SemanticColor.DANGER
        )

    }

    private fun clearInputs() {
        usernameInput.text = ""
        passwordInput.text = ""

        usernameInput.setError(false)
        passwordInput.setError(false)
    }

}


