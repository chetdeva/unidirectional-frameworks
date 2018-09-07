package com.chetdeva.unidirectionalframeworks

/**
 * @author chetansachdeva
 */
sealed class Event {
    data class EmailChanged(val email: String) : Event()
    data class PasswordChanged(val password: String) : Event()
    object LoginRequested: Event()
    object LoginSuccessful: Event()
    data class LoginFailed(val errorMessage: String): Event()
}