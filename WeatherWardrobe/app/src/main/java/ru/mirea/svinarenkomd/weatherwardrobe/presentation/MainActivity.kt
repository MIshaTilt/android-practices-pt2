package ru.mirea.svinarenkomd.weatherwardrobe.presentation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ru.mirea.svinarenkomd.weatherwardrobe.R
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.auth.AuthActivity
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.main.MainViewModel
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.main.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "MainActivity created")

        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(this)
        ).get(MainViewModel::class.java)

        val textUserStatus = findViewById<TextView>(R.id.textViewUserStatus)
        val editCity = findViewById<EditText>(R.id.editTextCity)
        val textOutput = findViewById<TextView>(R.id.textViewOutput)
        val buttonLoadWeather = findViewById<Button>(R.id.buttonLoadWeather)
        val buttonSaveWardrobe = findViewById<Button>(R.id.buttonSaveWardrobe)
        val buttonRecognizePhoto = findViewById<Button>(R.id.buttonRecognizePhoto)
        val buttonBackToAuth = findViewById<Button>(R.id.buttonBackToAuth)

        // 1. Подписка на LiveData профиля пользователя
        mainViewModel.userProfileState.observe(this) { profile ->
            val roleText = if (profile.isAuthorized) {
                "Авторизован (${profile.email})"
            } else {
                "Режим Гостя (только просмотр)"
            }
            textUserStatus.text =
                "Профиль (LiveData): $roleText | Любимый город: ${profile.preferredCity}"
            if (savedInstanceState == null && editCity.text.isNullOrBlank()) {
                editCity.setText(profile.preferredCity)
            }
        }

        // 2. Подписка на MediatorLiveData, объединяющую сетевой каталог (NetworkApi) и локальную БД (Room)
        mainViewModel.dashboardTextState.observe(this) { summaryText ->
            textOutput.text = summaryText
        }

        // 3. Подписка на LiveData статусов действий (сохранение в Room / AI-распознавание)
        mainViewModel.actionStatusState.observe(this) { statusMsg ->
            if (statusMsg.isNotBlank()) {
                Toast.makeText(this, statusMsg, Toast.LENGTH_SHORT).show()
                val currentText = mainViewModel.dashboardTextState.value.orEmpty()
                textOutput.text = ">>> $statusMsg\n\n$currentText"
            }
        }

        buttonLoadWeather.setOnClickListener {
            val city = editCity.text.toString().ifBlank { "Москва" }
            mainViewModel.loadWeatherAndCatalog(city)
        }

        buttonSaveWardrobe.setOnClickListener {
            mainViewModel.saveWindbreakerToWardrobe()
        }

        buttonRecognizePhoto.setOnClickListener {
            mainViewModel.recognizeSamplePhoto()
        }

        buttonBackToAuth.setOnClickListener {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
