package ru.mirea.svinarenkomd.weatherwardrobe.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ru.mirea.svinarenkomd.weatherwardrobe.R
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.MainActivity
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.main.ViewModelFactory

class AuthActivity : AppCompatActivity() {

    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        Log.d(TAG, "AuthActivity created")

        authViewModel = ViewModelProvider(
            this,
            ViewModelFactory(this)
        ).get(AuthViewModel::class.java)

        val textStatus = findViewById<TextView>(R.id.textViewAuthStatus)
        val editEmail = findViewById<EditText>(R.id.editTextEmail)
        val editPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)
        val buttonContinueToMain = findViewById<Button>(R.id.buttonContinueToMain)
        val buttonGuest = findViewById<Button>(R.id.buttonGuest)

        // Подписка на LiveData профиля пользователя
        authViewModel.userProfileState.observe(this) { profile ->
            if (profile.isAuthorized) {
                textStatus.text =
                    "Статус (LiveData): Авторизован (${profile.email})\nГород в SharedPreferences: ${profile.preferredCity}"
                buttonContinueToMain.visibility = View.VISIBLE
            } else {
                textStatus.text =
                    "Статус (LiveData): Не авторизован (Гость)\nГород в SharedPreferences: ${profile.preferredCity}"
                buttonContinueToMain.visibility = View.GONE
            }
        }

        // Подписка на LiveData сообщений авторизации
        authViewModel.authMessageState.observe(this) { message ->
            if (message.isNotBlank()) {
                textStatus.text = message
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }

        // Подписка на событие перехода на главный экран
        authViewModel.navigateToMainEvent.observe(this) { shouldNavigate ->
            if (shouldNavigate == true) {
                authViewModel.onNavigatedToMain()
                startActivity(Intent(this, MainActivity::class.java))
            }
        }

        buttonLogin.setOnClickListener {
            val email = editEmail.text.toString().trim()
            val password = editPassword.text.toString()
            authViewModel.login(email, password)
        }

        buttonRegister.setOnClickListener {
            val email = editEmail.text.toString().trim()
            val password = editPassword.text.toString()
            authViewModel.register(email, password)
        }

        buttonContinueToMain.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        buttonGuest.setOnClickListener {
            authViewModel.continueAsGuest()
        }
    }

    companion object {
        private const val TAG = "AuthActivity"
    }
}
