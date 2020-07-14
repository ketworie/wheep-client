package com.ketworie.wheep.client

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.postDelayed
import com.google.android.material.snackbar.Snackbar
import com.ketworie.wheep.client.MainApplication.Companion.IS_NEW_SESSION
import com.ketworie.wheep.client.hub.activity.HubListActivity
import com.ketworie.wheep.client.security.AuthInterceptor
import com.ketworie.wheep.client.security.SecurityService
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignInActivity : AppCompatActivity() {

    @Inject
    lateinit var securityService: SecurityService

    @Inject
    lateinit var authInterceptor: AuthInterceptor

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        toLoggedState()
        appName.postDelayed(200) { checkToken() }


        findViewById<EditText>(R.id.password).setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                processSignIn()
                return@setOnEditorActionListener true
            }
            false
        }

    }

    private fun retrieveToken(): String {
        return getPreferences(Context.MODE_PRIVATE).getString(MainApplication.X_AUTH_TOKEN, "")
            .orEmpty()
    }

    private fun persistToken(token: String) {
        getPreferences(Context.MODE_PRIVATE).edit().putString(MainApplication.X_AUTH_TOKEN, token)
            .apply()
    }


    private fun toLoggedState() {
        appName.y = resources.displayMetrics.heightPixels.toFloat() * 0.3f
        login.alpha = 0f
        password.alpha = 0f
        signInButton.alpha = 0f
    }

    private fun checkToken() {
        val token = retrieveToken()
        if (token.isNotEmpty()) {
            startHubList(token, false)
            return
        }
        toUnLoggedState()
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
                runOnUiThread { startHubList(token, true) }
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

    private fun lockSignInButton() {
        signInButton.isEnabled = false
        signInButton.text = resources.getString(R.string.login_button_signing)
    }

    private fun unlockSignInButton() {
        signInButton.isEnabled = true
        signInButton.text = resources.getString(R.string.login_button_waiting)
    }

    private fun hideKeyboard() {
        getSystemService(InputMethodManager::class.java)?.hideSoftInputFromWindow(
            this.currentFocus?.windowToken,
            0
        )
    }

    private fun startHubList(token: String, isNewSession: Boolean) {
        persistToken(token)
        authInterceptor.token = token
        startActivity(
            Intent(this, HubListActivity::class.java).putExtra(IS_NEW_SESSION, isNewSession)
            , ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle()
        )
    }

    private fun hasValidInput(): Boolean {
        if (login.text.isNullOrBlank()) {
            login.requestFocus()
            login.error = resources.getString(R.string.login_empty)
            return false
        }
        if (password.text.isNullOrBlank()) {
            password.requestFocus()
            password.error = resources.getString(R.string.password_empty)
            return false
        }
        return true
    }
}
