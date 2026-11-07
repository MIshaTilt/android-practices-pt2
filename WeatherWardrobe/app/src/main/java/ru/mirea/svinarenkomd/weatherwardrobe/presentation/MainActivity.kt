package ru.mirea.svinarenkomd.weatherwardrobe.presentation

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ru.mirea.svinarenkomd.weatherwardrobe.R
import ru.mirea.svinarenkomd.weatherwardrobe.data.repository.AuthRepositoryImpl
import ru.mirea.svinarenkomd.weatherwardrobe.data.repository.ClassifierRepositoryImpl
import ru.mirea.svinarenkomd.weatherwardrobe.data.repository.ClothingRepositoryImpl
import ru.mirea.svinarenkomd.weatherwardrobe.data.repository.UserRepositoryImpl
import ru.mirea.svinarenkomd.weatherwardrobe.data.repository.WeatherRepositoryImpl
import ru.mirea.svinarenkomd.weatherwardrobe.domain.models.ClothingItem
import ru.mirea.svinarenkomd.weatherwardrobe.domain.usecases.GetClothingCatalogUseCase
import ru.mirea.svinarenkomd.weatherwardrobe.domain.usecases.GetClothingDetailsUseCase
import ru.mirea.svinarenkomd.weatherwardrobe.domain.usecases.GetCurrentWeatherUseCase
import ru.mirea.svinarenkomd.weatherwardrobe.domain.usecases.GetUserProfileUseCase
import ru.mirea.svinarenkomd.weatherwardrobe.domain.usecases.GetUserWardrobeUseCase
import ru.mirea.svinarenkomd.weatherwardrobe.domain.usecases.LoginUseCase
import ru.mirea.svinarenkomd.weatherwardrobe.domain.usecases.LogoutUseCase
import ru.mirea.svinarenkomd.weatherwardrobe.domain.usecases.RecognizeClothingByPhotoUseCase
import ru.mirea.svinarenkomd.weatherwardrobe.domain.usecases.SaveClothingToWardrobeUseCase
import ru.mirea.svinarenkomd.weatherwardrobe.domain.usecases.SavePreferredCityUseCase

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editTextCity = findViewById<EditText>(R.id.editTextCity)
        val textViewOutput = findViewById<TextView>(R.id.textViewOutput)

        // Инициализация репозиториев (Context передаётся только в data-слой)
        val authRepository = AuthRepositoryImpl(this)
        val userRepository = UserRepositoryImpl(this)
        val weatherRepository = WeatherRepositoryImpl()
        val clothingRepository = ClothingRepositoryImpl()
        val classifierRepository = ClassifierRepositoryImpl()

        // Инициализация UseCase-классов слоя domain
        val getUserProfileUseCase = GetUserProfileUseCase(userRepository)
        val savePreferredCityUseCase = SavePreferredCityUseCase(userRepository)
        val getCurrentWeatherUseCase = GetCurrentWeatherUseCase(weatherRepository)
        val getClothingCatalogUseCase = GetClothingCatalogUseCase(clothingRepository)
        val getClothingDetailsUseCase = GetClothingDetailsUseCase(clothingRepository)
        val getUserWardrobeUseCase = GetUserWardrobeUseCase(clothingRepository)
        val saveClothingToWardrobeUseCase = SaveClothingToWardrobeUseCase(clothingRepository)
        val recognizeClothingByPhotoUseCase = RecognizeClothingByPhotoUseCase(classifierRepository)
        val loginUseCase = LoginUseCase(authRepository)
        val logoutUseCase = LogoutUseCase(authRepository)

        val initialProfile = getUserProfileUseCase.execute()
        editTextCity.setText(initialProfile.preferredCity)

        fun renderSummary(extraNote: String = "") {
            val profile = getUserProfileUseCase.execute()
            val activeCity = if (profile.isAuthorized) profile.preferredCity else "Москва (по умолчанию для Гостя)"
            val weather = getCurrentWeatherUseCase.execute(if (profile.isAuthorized) profile.preferredCity else "Москва")
            val recommended = getClothingCatalogUseCase.execute(weather.temperature)

            val sb = StringBuilder()
            if (extraNote.isNotEmpty()) {
                sb.appendLine(extraNote)
                sb.appendLine("---")
            }
            sb.appendLine("Режим: ${if (profile.isAuthorized) "Авторизован (${profile.email})" else "Гость"}")
            sb.appendLine("Город: $activeCity")
            sb.appendLine("Погода: ${weather.city}, ${weather.temperature}°C, ${weather.condition}, ветер ${weather.windSpeed} м/с")
            sb.appendLine()
            sb.appendLine("Рекомендовано по погоде (${recommended.size} шт.):")
            recommended.forEach { item ->
                sb.appendLine(" • ${item.name} [${item.minTemp}°C..${item.maxTemp}°C] (${item.category})")
            }
            sb.appendLine()
            if (profile.isAuthorized) {
                val wardrobe = getUserWardrobeUseCase.execute()
                sb.appendLine("Мой гардероб (${wardrobe.size} шт.):")
                wardrobe.forEach { item ->
                    sb.appendLine(" • #${item.id} ${item.name} (${item.category})")
                }
            } else {
                sb.appendLine("Мой гардероб и ИИ-сканер: недоступны для Гостя (требуется вход)")
            }
            textViewOutput.text = sb.toString()
        }

        renderSummary("Каркас WeatherWardrobe успешно инициализирован.")

        findViewById<Button>(R.id.buttonSaveCity).setOnClickListener {
            val profile = getUserProfileUseCase.execute()
            if (!profile.isAuthorized) {
                renderSummary("Ошибка доступа: Гость не может менять город по умолчанию (доступна только Москва).")
                return@setOnClickListener
            }
            val cityInput = editTextCity.text.toString()
            val saved = savePreferredCityUseCase.execute(cityInput)
            renderSummary("SavePreferredCityUseCase: result = $saved")
        }

        findViewById<Button>(R.id.buttonShowCatalog).setOnClickListener {
            val allCatalog = getClothingCatalogUseCase.execute()
            val firstDetails = getClothingDetailsUseCase.execute(1)
            val note = buildString {
                appendLine("Весь каталог (${allCatalog.size} вещей):")
                allCatalog.forEach { appendLine(" - ${it.name}: ${it.minTemp}..${it.maxTemp}°C") }
                if (firstDetails != null) {
                    append("Детали вещи #1: ${firstDetails.description}")
                }
            }
            renderSummary(note)
        }

        findViewById<Button>(R.id.buttonTestAiScanner).setOnClickListener {
            val profile = getUserProfileUseCase.execute()
            if (!profile.isAuthorized) {
                renderSummary("Ошибка доступа: ИИ-сканер (TFLite) и сохранение в гардероб доступны только авторизованным пользователям.")
                return@setOnClickListener
            }
            val aiResult = recognizeClothingByPhotoUseCase.execute("trench")
            val newItem = ClothingItem(
                id = 0,
                name = "Распознано ИИ: ${aiResult.detectedLabel}",
                category = aiResult.mappedCategory,
                minTemp = aiResult.recommendedMinTemp,
                maxTemp = aiResult.recommendedMaxTemp,
                imageUrl = "",
                description = "Точность TFLite: ${(aiResult.confidence * 100).toInt()}%"
            )
            val saved = saveClothingToWardrobeUseCase.execute(newItem)
            renderSummary(
                "RecognizeClothingByPhotoUseCase:\n" +
                    "Класс: ${aiResult.detectedLabel} (${(aiResult.confidence * 100).toInt()}%)\n" +
                    "Категория: ${aiResult.mappedCategory}\n" +
                    "Сохранено в гардероб: $saved"
            )
        }

        findViewById<Button>(R.id.buttonToggleAuth).setOnClickListener {
            val current = getUserProfileUseCase.execute()
            if (current.isAuthorized) {
                logoutUseCase.execute()
                renderSummary("Выполнен LogoutUseCase -> Режим Гостя")
            } else {
                loginUseCase.execute("svinarenko@mirea.ru", "123456")
                renderSummary("Выполнен LoginUseCase -> Авторизован (svinarenko@mirea.ru)")
            }
        }
    }
}
