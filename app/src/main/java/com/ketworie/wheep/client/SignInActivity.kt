package com.ketworie.wheep.client

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.ketworie.wheep.client.MainApplication.Companion.X_AUTH_TOKEN
import com.ketworie.wheep.client.dao.SecurityService
import dagger.android.AndroidInjection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignInActivity : AppCompatActivity() {

    private var isLogged = false
    private var token = ""

    private lateinit var appNameView: TextView
    private lateinit var loginView: EditText
    private lateinit var passwordView: EditText
    private lateinit var signInButton: Button
    private lateinit var rootLayout: ConstraintLayout

    @Inject
    lateinit var securityService: SecurityService

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        appNameView = findViewById(R.id.appNameText)
        loginView = findViewById(R.id.loginText)
        passwordView = findViewById(R.id.passwordText)
        signInButton = findViewById(R.id.signInButton)
        rootLayout = findViewById(R.id.signInRootLayout)
        toLoggedState()


        findViewById<EditText>(R.id.passwordText).setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                processSignIn()
                return@setOnEditorActionListener true
            }
            false
        }

    }

    private fun toLoggedState() {
        appNameView.y = resources.displayMetrics.heightPixels.toFloat() * 0.4f
        loginView.alpha = 0f
        passwordView.alpha = 0f
        signInButton.alpha = 0f
    }

    private fun toUnLoggedState() {
        appNameView.animate().y(resources.displayMetrics.heightPixels.toFloat() * 0.18f)
            .setDuration(1000).withEndAction {
                loginView.animate().alpha(1f).start()
                passwordView.animate().alpha(1f).start()
                signInButton.animate().alpha(1f).start()
            }.start()
    }


    override fun onResume() {
        if (!isLogged)
            toUnLoggedState()
        super.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
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
                token =
                    securityService.login(loginView.text.toString(), passwordView.text.toString())
                startChat(token)
            } catch (e: Exception) {
                Snackbar
                    .make(appNameView, e.toString(), Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(
                        resources.getColor(
                            R.color.design_default_color_error,
                            resources.newTheme()
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

    private fun startChat(token: String) {
        val intent = Intent(this, MessageActivity::class.java).apply {
            putExtra(X_AUTH_TOKEN, token)
        }
        startActivity(intent)
    }


    private fun hasValidInput(): Boolean {
        if (loginView.text.isNullOrBlank()) {
            loginView.requestFocus()
            loginView.error = resources.getString(R.string.login_empty)
            return false
        }
        if (passwordView.text.isNullOrBlank()) {
            passwordView.requestFocus()
            passwordView.error = resources.getString(R.string.password_empty)
            return false
        }
        return true
    }
}
