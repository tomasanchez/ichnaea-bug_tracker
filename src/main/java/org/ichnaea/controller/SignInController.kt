package org.ichnaea.controller

import org.ichnaea.auth.IchnaeaSessionProvider
import org.ichnaea.core.exception.AuthenticationException
import org.ichnaea.core.mvc.controller.UIController
import org.ichnaea.core.security.auth.Authentication
import org.ichnaea.core.ui.semantic.SemanticColor
import org.ichnaea.form.UserForm
import org.ichnaea.service.UserService
import org.slf4j.Logger
import java.awt.event.ActionEvent


@UIController
class SignInController : BaseController() {

    private val userService = UserService()
    private val sessionProvider: IchnaeaSessionProvider =
        getSecurityContext().sessionManager as IchnaeaSessionProvider
    private lateinit var userForm: UserForm

    companion object {
        private val logger: Logger = org.slf4j.LoggerFactory.getLogger(SignInController::class.java)
    }

    // -------------------------------------------------------------
    // Lifecycle Methods
    // -------------------------------------------------------------

    override fun onInit() {
        userForm = view.model["userForm"] as UserForm
        userForm.submitButton.onClick(::onSignIn)
    }

    override fun onBeforeRendering() {
    }

    override fun onAfterRendering() {

    }

    // ------------------------------
    // Event Handlers
    // ------------------------------

    private fun onSignIn(event: ActionEvent) {

        val username = userForm.username.text
        val password = userForm.password.value()

        val anyEmptyFields = validateEmpty(username, userForm.username) || validateEmpty(password, userForm.password)

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

    }

    // ------------------------------
    // Internal Methods
    // ------------------------------

    private fun onSuccessfulSignIn(auth: Authentication) {
        logger.info("Sign in successful for ${auth.name}")

        val user = userService.findByUsername(auth.name)

        sessionProvider.session = user.get()
        userForm.clear()
        navTo("Projects")
    }

    private fun onFailedSignIn(e: AuthenticationException) {

        logger.error("Sign in failed")

        userForm.username.setError(true)
        userForm.password.setError(true)
        userForm.password.text = ""

        showAlert(
            title = "Error",
            message = "Sign in failed: ${e.message}",
            color = SemanticColor.DANGER
        )

    }
}


