package com.chetdeva.unidirectionalframeworks

/**
 * @author chetansachdeva
 */
sealed class Effect {
    data class AttemptLogin(val email: String, val password: String) : Effect()
    data class ShowErrorMessage(val message: String) : Effect()
    object NavigateToHome: Effect()
}