package com.chetdeva.unidirectionalframeworks

import io.reactivex.Observable


/**
 * @author chetansachdeva
 */

class LoginService {
    fun login(email: String, password: String): Observable<Login> {
        val login = email == "chetan@gmail.com" && password == "1234"
        return if (login) {
            Observable.just(Login.Success)
        } else {
            Observable.just(Login.Failure("Invalid credentials"))
        }
    }
}

sealed class Login {
    object Success : Login()
    data class Failure(val message: String) : Login()
}