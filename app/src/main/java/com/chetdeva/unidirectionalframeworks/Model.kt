package com.chetdeva.unidirectionalframeworks

/**
 * @author chetansachdeva
 */
data class Model(val email: String, val password: String, val canLogin: Boolean, val loggingIn: Boolean) {
    companion object {
        fun default(): Model {
            return Model(email = "",
                    password = "",
                    canLogin = false,
                    loggingIn = false)
        }
    }
}

data class LoginViewData(val email: String, val password: String, val canLogin: Boolean, val loggingIn: Boolean)

fun toViewData(model: Model): LoginViewData {
    return LoginViewData(model.email, model.password, model.canLogin, model.loggingIn)
}
