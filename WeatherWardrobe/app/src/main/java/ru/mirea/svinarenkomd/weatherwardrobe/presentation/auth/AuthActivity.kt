package ru.mirea.svinarenkomd.weatherwardrobe.presentation.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ru.mirea.svinarenkomd.weatherwardrobe.databinding.ActivityAuthBinding
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.MainActivity
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.main.ViewModelFactory

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "AuthActivity created")

        authViewModel = ViewModelProvider(
            this,
            ViewModelFactory(this)
        ).get(AuthViewModel::class.java)

        authViewModel.userProfileState.observe(this) { profile ->
            if (profile.isAuthorized) {
                binding.textViewAuthStatus.text =
                    "Статус (LiveData): Авторизован (${profile.email})\nГород в SharedPreferences: ${profile.preferredCity}"
                binding.buttonContinueToMain.visibility = View.VISIBLE
            } else {
                binding.textViewAuthStatus.text =
                    "Статус (LiveData): Не авторизован (Гость)\nГород в SharedPreferences: ${profile.preferredCity}"
                binding.buttonContinueToMain.visibility = View.GONE
            }
        }

        authViewModel.authMessageState.observe(this) { message ->
            if (message.isNotBlank()) {
                binding.textViewAuthStatus.text = message
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }

        authViewModel.navigateToMainEvent.observe(this) { shouldNavigate ->
            if (shouldNavigate == true) {
                authViewModel.onNavigatedToMain()
                startActivity(Intent(this, MainActivity::class.java))
            }
        }

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString()
            authViewModel.login(email, password)
        }

        binding.buttonRegister.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString()
            authViewModel.register(email, password)
        }

        binding.buttonContinueToMain.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.buttonGuest.setOnClickListener {
            authViewModel.continueAsGuest()
        }
    }

    companion object {
        private const val TAG = "AuthActivity"
    }
}
