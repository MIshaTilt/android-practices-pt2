package ru.mirea.svinarenkomd.weatherwardrobe.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.mirea.svinarenkomd.data.repository.AuthRepositoryImpl
import ru.mirea.svinarenkomd.data.repository.UserRepositoryImpl
import ru.mirea.svinarenkomd.data.storage.sharedprefs.SharedPrefUserStorage
import ru.mirea.svinarenkomd.domain.usecases.GetUserProfileUseCase
import ru.mirea.svinarenkomd.domain.usecases.LoginUseCase
import ru.mirea.svinarenkomd.domain.usecases.LogoutUseCase
import ru.mirea.svinarenkomd.domain.usecases.RegisterUseCase
import ru.mirea.svinarenkomd.weatherwardrobe.R
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.MainActivity

class AuthActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val userStorage = SharedPrefUserStorage(this)
        val authRepository = AuthRepositoryImpl(userStorage)
        val userRepository = UserRepositoryImpl(userStorage)

        val loginUseCase = LoginUseCase(authRepository)
        val registerUseCase = RegisterUseCase(authRepository)
        val logoutUseCase = LogoutUseCase(authRepository)
        val getUserProfileUseCase = GetUserProfileUseCase(userRepository)

        val textStatus = findViewById<TextView>(R.id.textViewAuthStatus)
        val editEmail = findViewById<EditText>(R.id.editTextEmail)
        val editPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)
        val buttonContinueToMain = findViewById<Button>(R.id.buttonContinueToMain)
        val buttonGuest = findViewById<Button>(R.id.buttonGuest)

        fun refreshStatus() {
            val profile = getUserProfileUseCase.execute()
            if (profile.isAuthorized) {
                textStatus.text = "Статус: Авторизован (${profile.email})\nГород в SharedPreferences: ${profile.preferredCity}"
                buttonContinueToMain.visibility = View.VISIBLE
            } else {
                textStatus.text = "Статус: Не авторизован (Гость)\nГород в SharedPreferences: ${profile.preferredCity}"
                buttonContinueToMain.visibility = View.GONE
            }
        }

        refreshStatus()

        buttonLogin.setOnClickListener {
            val email = editEmail.text.toString().trim()
            val password = editPassword.text.toString()
            textStatus.text = "Выполняется вход через Firebase Auth..."
            loginUseCase.execute(email, password) { success, message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                refreshStatus()
                if (success) {
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    textStatus.text = message
                }
            }
        }

        buttonRegister.setOnClickListener {
            val email = editEmail.text.toString().trim()
            val password = editPassword.text.toString()
            textStatus.text = "Создание аккаунта в Firebase Auth..."
            registerUseCase.execute(email, password) { success, message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                refreshStatus()
                if (success) {
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    textStatus.text = message
                }
            }
        }

        buttonContinueToMain.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        buttonGuest.setOnClickListener {
            logoutUseCase.execute()
            Toast.makeText(this, "Вход в режиме Гостя", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
