package com.chetdeva.unidirectionalframeworks

import android.util.Log
import com.spotify.mobius.Connectable
import com.spotify.mobius.Connection
import com.spotify.mobius.functions.Consumer
import io.reactivex.Observable

/**
 * @author chetansachdeva
 */
class MainView: Connectable<LoginViewData, Event> {

    override fun connect(output: Consumer<Event>): Connection<LoginViewData> {

        return onAccept<LoginViewData> {
            render(it)
        }.onDispose {
        }
    }

    private fun render(loginViewData: LoginViewData) {
        Log.i("MainView", "$loginViewData")
    }
}