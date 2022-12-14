package org.ichnaea.controller

import org.ichnaea.core.exception.UserAlreadyExistsException
import org.ichnaea.core.mvc.controller.UIController
import org.ichnaea.core.ui.form.PasswordField
import org.ichnaea.core.ui.form.TextField
import org.ichnaea.core.ui.semantic.Notification
import org.ichnaea.core.ui.semantic.SemanticColor
import org.ichnaea.model.User
import org.ichnaea.service.UserService
import org.slf4j.Logger
import java.awt.event.ActionEvent

@UIController
class SignUpController : SideViewController() {

    private lateinit var usernameInput: TextField
    private lateinit var passwordInput: PasswordField
    private lateinit var confirmPasswordInput: PasswordField

    private val userDetailsService: UserService = UserService()

    private var alertMessage = SUCCESS_MESSAGE

    companion object {
        private val logger: Logger = org.slf4j.LoggerFactory.getLogger(SignUpController::class.java)

        private const val SUCCESS_MESSAGE = "User was successfully created"
    }

    // -------------------------------------------------------------
    // Lifecycle Methods
    // -------------------------------------------------------------

    override fun onInit() {

        val signUpButton = this.byId("signUpButton") as org.ichnaea.core.ui.button.Button?
        signUpButton?.onClick(::onSignUp)

        val cancelButton = this.byId("cancelButton") as org.ichnaea.core.ui.button.Button?
        cancelButton?.onClick(::onCancel)

        usernameInput = this.byId("usernameField") as TextField
        passwordInput = this.byId("passwordField") as PasswordField
        confirmPasswordInput = this.byId("password2Field") as PasswordField

    }

    override fun onBeforeRendering() {

    }

    override fun onAfterRendering() {
        updateNavSelection(ADD_USER_NAV)
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

        if (hasError) {
            showAlert("Error", "Please fill out all fields", SemanticColor.DANGER)
            return
        }

        if (!doesPasswordMatch()) {
            showAlert("Error", "Passwords do not match", SemanticColor.DANGER)
            logger.error("Passwords do not match")
            return
        }


        var user = User(usernameInput.text.trim(), passwordInput.value().trim())

        try {

            user = userDetailsService.save(user)

        } catch (uae: UserAlreadyExistsException) {
            uae.message?.let {
                popNotification(
                    it,
                    type = Notification.Type.ERROR,
                    location = Notification.Location.BOTTOM_CENTER
                )
            }
            usernameInput.setError(true)
            logger.error("User already exists")
            return
        } catch (e: Exception) {
            popNotification(
                e.message ?: "Couldn't add the user. Try again later.",
                type = Notification.Type.ERROR,
            )
            logger.error("Error creating a user", e)
            return
        }

        popNotification(
            "User '${user.userName}' was created",
            type = Notification.Type.SUCCESS,
        )

        clearInputs()

        logger.info("User created: $user")
    }

    private fun onCancel(event: ActionEvent) {
        clearInputs()
    }

    // ------------------------------
    // Internal Methods
    // ------------------------------

    private fun doesPasswordMatch(): Boolean {
        return (passwordInput.value() == confirmPasswordInput.value()).also {
            confirmPasswordInput.setError(!it)
            passwordInput.setError(!it)
        }
    }


    private fun clearInputs() {
        usernameInput.text = ""
        passwordInput.text = ""
        confirmPasswordInput.text = ""

        usernameInput.setError(false)
        passwordInput.setError(false)
        confirmPasswordInput.setError(false)
    }


}