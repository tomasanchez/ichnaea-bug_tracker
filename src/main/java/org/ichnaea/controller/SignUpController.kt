package org.ichnaea.controller

import org.ichnaea.core.mvc.controller.Controller
import org.ichnaea.core.ui.form.PasswordField
import org.ichnaea.core.ui.form.TextField
import org.ichnaea.core.ui.semantic.SemanticColor
import org.ichnaea.model.User
import org.ichnaea.service.UserService
import org.slf4j.Logger
import java.awt.event.ActionEvent

@Controller
class SignUpController : BaseController() {

    private lateinit var usernameInput: TextField
    private lateinit var passwordInput: PasswordField
    private lateinit var confirmPasswordInput: PasswordField

    private val userDetailsService: UserService = UserService()

    private var alertMessage = SUCCESS_MESSAGE

    companion object {
        private val logger: Logger = org.slf4j.LoggerFactory.getLogger(SignUpController::class.java)

        private const val SUCCESS_MESSAGE = "User was successfully created"
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

        if (hasError) {
            showAlert("Error", "Please fill out all fields", SemanticColor.DANGER)
            return
        }

        if (doesPasswordMatch()) {
            var user = User(usernameInput.text, passwordInput.value())
            user = userDetailsService.save(user)
            showAlert("Success", "User was created successfully")
            clearInputs()
            logger.info("User created: $user")
        } else {
            showAlert("Error", "Passwords do not match", SemanticColor.DANGER)
            logger.error("Passwords do not match")
        }


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
        passwordInput.setError(false)
    }


}