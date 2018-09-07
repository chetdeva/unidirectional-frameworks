package com.chetdeva.unidirectionalframeworks

import com.spotify.mobius.Effects.effects
import com.spotify.mobius.Next
import com.spotify.mobius.Next.*

/**
 * @author chetansachdeva
 */

fun update(model: Model, event: Event): Next<Model, Effect> {
    return when (event) {
        is Event.EmailChanged -> onEmailChanged(model, event)
        is Event.PasswordChanged -> onPasswordChanged(model, event)
        is Event.LoginRequested -> onLoginRequested(model)
        is Event.LoginSuccessful -> onLoginSuccess(model)
        is Event.LoginFailed -> onLoginFailed(model, event)
    }
}

fun onLoginFailed(model: Model, event: Event.LoginFailed): Next<Model, Effect> {
    return dispatch(effects(Effect.ShowErrorMessage(event.errorMessage)))
}

fun onLoginSuccess(model: Model): Next<Model, Effect> {
    return dispatch(effects(Effect.NavigateToHome))
}

fun onLoginRequested(model: Model): Next<Model, Effect> {
    return if (!model.loggingIn && model.canLogin) {
        val newModel = model.copy(loggingIn = true)
        next(newModel,
                effects(Effect.AttemptLogin(model.email, model.password)))
    } else {
        dispatch(effects(Effect.ShowErrorMessage("Can't login")))
    }
}

private fun onEmailChanged(model: Model, event: Event.EmailChanged): Next<Model, Effect> {
    val newModel = model.copy(email = event.email,
            canLogin = verifyCredentials(event.email, model.password))
    return next(newModel)
}

private fun onPasswordChanged(model: Model, event: Event.PasswordChanged): Next<Model, Effect> {
    val newModel = model.copy(password = event.password,
            canLogin = verifyCredentials(model.email, event.password))
    return next(newModel)
}

fun verifyCredentials(email: String, password: String): Boolean {
    return email.isNotBlank() && password.isNotBlank()
}