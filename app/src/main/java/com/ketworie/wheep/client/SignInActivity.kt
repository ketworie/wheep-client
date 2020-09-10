package com.ketworie.wheep.client

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.google.android.material.snackbar.Snackbar
import com.ketworie.wheep.client.MainApplication.Companion.PREFERENCES
import com.ketworie.wheep.client.MainApplication.Companion.USER_ID
import com.ketworie.wheep.client.MainApplication.Companion.X_AUTH_TOKEN
import com.ketworie.wheep.client.network.NetworkResponse
import com.ketworie.wheep.client.network.errorMessage
import com.ketworie.wheep.client.security.AuthInterceptor
import com.ketworie.wheep.client.security.SecurityService
import com.ketworie.wheep.client.user.UserService
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignInActivity : AppCompatActivity() {

    @Inject
    lateinit var securityService: SecurityService

    @Inject
    lateinit var userService: UserService

    @Inject
    lateinit var authInterceptor: AuthInterceptor

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        toLoggedState()
        appName.post { recoverSession() }


        password.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                processSignIn()
                return@setOnEditorActionListener true
            }
            false
        }

    }

    private fun retrieveToken(): String {
        return getSharedPreferences(
            PREFERENCES,
            Context.MODE_PRIVATE
        ).getString(X_AUTH_TOKEN, "").orEmpty()
    }

    private fun retrieveUserId(): String {
        return getSharedPreferences(
            PREFERENCES,
            Context.MODE_PRIVATE
        ).getString(USER_ID, "").orEmpty()
    }

    private fun persistToken(token: String) {
        getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).edit()
            .putString(X_AUTH_TOKEN, token)
            .apply()
    }


    private fun toLoggedState() {
        appName.y = resources.displayMetrics.heightPixels.toFloat() * 0.3f
        login.alpha = 0f
        password.alpha = 0f
        signInButton.alpha = 0f
    }

    private fun recoverSession() {
        val token = retrieveToken()
        val userId = retrieveUserId()
        if (token.isEmpty() || userId.isEmpty()) {
            toUnLoggedState()
            return
        }
        registerToken(token)
        CoroutineScope(Dispatchers.IO).launch {
            when (val response = userService.loadMe()) {
                is NetworkResponse.Success -> {
                    registerUserId(response.body.id)
                    withContext(Dispatchers.Main) { startHomeActivity() }
                    return@launch
                }
                is NetworkResponse.ApiError -> {
                    withContext(Dispatchers.Main) { toUnLoggedState() }
                    return@launch
                }
            }
            registerUserId(userId)
            withContext(Dispatchers.Main) { startHomeActivity() }
        }
    }

    private fun toUnLoggedState() {
        appName.animate().y(resources.displayMetrics.heightPixels.toFloat() * 0.18f)
            .setDuration(500)
            .withEndAction {
                login.animate().alpha(1f).start()
                password.animate().alpha(1f).start()
                signInButton.animate().alpha(1f).start()
            }
            .start()
    }

    fun onSignIn(view: android.view.View) {
        processSignIn()
    }

    private fun processSignIn() {
        if (!hasValidInput()) return
        hideKeyboard()
        lockSignInButton()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val token =
                    securityService.login(login.text.toString(), password.text.toString())
                persistToken(token)
                registerToken(token)
                loadMe()
                runOnUiThread { startHomeActivity() }
            } catch (e: Exception) {
                Snackbar
                    .make(appName, e.message!!, Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(
                        resources.getColor(
                            R.color.design_default_color_error,
                            theme
                        )
                    )
                    .show()
            }
            runOnUiThread {
                unlockSignInButton()
            }
        }
    }

    private suspend fun loadMe() {
        when (val response = userService.loadMe()) {
            is NetworkResponse.Success -> {
                val userId = response.body.id
                getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).edit()
                    .putString(USER_ID, userId).apply()
                registerUserId(userId)
            }
            else -> throw RuntimeException(response.errorMessage(this))
        }
    }

    private fun registerToken(token: String) {
        authInterceptor.token = token
    }

    private fun registerUserId(userId: String) {
        userService.userId = userId
    }

    private fun lockSignInButton() {
        signInButton.isEnabled = false
        signInButton.text = resources.getString(R.string.login_button_signing)
    }

    private fun unlockSignInButton() {
        signInButton.isEnabled = true
        signInButton.text = resources.getString(R.string.login_button_waiting)
    }

    private fun startHomeActivity() {
        startActivity(
            Intent(this, HomeActivity::class.java)
            , ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle()
        )
    }

    private fun hasValidInput(): Boolean {
        return password.requireNonBlank(resources.getString(R.string.password_empty)) &&
                login.requireNonBlank(resources.getString(R.string.login_empty))
    }
}
