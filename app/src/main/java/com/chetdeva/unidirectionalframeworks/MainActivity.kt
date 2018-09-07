package com.chetdeva.unidirectionalframeworks

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.spotify.mobius.EventSource
import com.spotify.mobius.MobiusLoop
import com.spotify.mobius.android.AndroidLogger
import com.spotify.mobius.android.MobiusAndroid
import com.spotify.mobius.rx2.RxMobius
import io.reactivex.ObservableTransformer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var controller: MobiusLoop.Controller<Model, Event>
    private val loginService = LoginService()
    private val mEventSource = DeferredEventSource<Event>()
    private lateinit var mViews: MainView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mViews = MainView()

        val effectHandler = effectHandler()
        controller = createController(effectHandler, mEventSource, Model.default())
        controller.connect(mViews.contramap(::toViewData))

        registerEvents()
    }

    private fun registerEvents() {
        email.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                mEventSource.notifyEvent(Event.EmailChanged(p0.toString()))
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        password.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                mEventSource.notifyEvent(Event.PasswordChanged(p0.toString()))
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        login.setOnClickListener { mEventSource.notifyEvent(Event.LoginRequested) }
    }

    fun createController(effectHandler: ObservableTransformer<Effect, Event>, eventSource: EventSource<Event>, defaultModel: Model
    ): MobiusLoop.Controller<Model, Event> {
        return MobiusAndroid.controller(createLoop(eventSource, effectHandler), defaultModel)
    }


    private fun createLoop(eventSource: EventSource<Event>, effectHandler: ObservableTransformer<Effect, Event>
    ): MobiusLoop.Factory<Model, Event, Effect> =
            loopFactory(::update, effectHandler)
                    .eventSource(eventSource)
                    .logger(AndroidLogger.tag("TasksList"))

    private fun effectHandler(): ObservableTransformer<Effect, Event> {
        return RxMobius.subtypeEffectHandler<Effect, Event>()
                .addAction(Effect.NavigateToHome.javaClass) { showHome() }
                .addConsumer(Effect.ShowErrorMessage::class.java) { showError(it.message) }
                .addTransformer(Effect.AttemptLogin::class.java, loginHandler())
                .build()
    }

    private fun loginHandler(): ObservableTransformer<Effect.AttemptLogin, Event> {
        return ObservableTransformer {
            it.switchMap {
                loginService.login(it.email, it.password)
                        .map { login ->
                            when (login) {
                                is Login.Success -> Event.LoginSuccessful
                                is Login.Failure -> Event.LoginFailed(login.message)
                            }
                        }
            }
        }
    }

    fun onLoginClick(view: View) {
        mEventSource.notifyEvent(Event.LoginRequested)
    }

    private fun showError(message: String) {
        Log.i("MainActivity", "error $message")
    }

    fun showHome() {
        Log.i("MainActivity", "showing home")
    }

    override fun onResume() {
        super.onResume()
        controller.start()
    }

    override fun onPause() {
        controller.stop()
        super.onPause()
    }
}
