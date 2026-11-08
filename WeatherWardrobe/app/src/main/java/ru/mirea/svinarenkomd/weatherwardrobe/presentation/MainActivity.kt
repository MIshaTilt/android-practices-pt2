package ru.mirea.svinarenkomd.weatherwardrobe.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ru.mirea.svinarenkomd.data.network.NetworkApi
import ru.mirea.svinarenkomd.data.repository.ClassifierRepositoryImpl
import ru.mirea.svinarenkomd.data.repository.ClothingRepositoryImpl
import ru.mirea.svinarenkomd.data.repository.UserRepositoryImpl
import ru.mirea.svinarenkomd.data.repository.WeatherRepositoryImpl
import ru.mirea.svinarenkomd.data.storage.room.RoomClothingStorage
import ru.mirea.svinarenkomd.data.storage.sharedprefs.SharedPrefUserStorage
import ru.mirea.svinarenkomd.domain.models.ClothingItem
import ru.mirea.svinarenkomd.domain.usecases.GetClothingCatalogUseCase
import ru.mirea.svinarenkomd.domain.usecases.GetClothingDetailsUseCase
import ru.mirea.svinarenkomd.domain.usecases.GetCurrentWeatherUseCase
import ru.mirea.svinarenkomd.domain.usecases.GetUserProfileUseCase
import ru.mirea.svinarenkomd.domain.usecases.GetUserWardrobeUseCase
import ru.mirea.svinarenkomd.domain.usecases.RecognizeClothingByPhotoUseCase
import ru.mirea.svinarenkomd.domain.usecases.SaveClothingToWardrobeUseCase
import ru.mirea.svinarenkomd.domain.usecases.SavePreferredCityUseCase
import ru.mirea.svinarenkomd.weatherwardrobe.R
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.auth.AuthActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Инициализация 3 механизмов хранения/получения данных в модуле :data
        val userStorage = SharedPrefUserStorage(this)
        val clothingStorage = RoomClothingStorage(this)
        val networkApi = NetworkApi()

        // Репозитории из модуля :data
        val clothingRepository = ClothingRepositoryImpl(clothingStorage, networkApi)
        val weatherRepository = WeatherRepositoryImpl(networkApi)
        val userRepository = UserRepositoryImpl(userStorage)
        val classifierRepository = ClassifierRepositoryImpl()

        // UseCases из модуля :domain
        val getCurrentWeatherUseCase = GetCurrentWeatherUseCase(weatherRepository)
        val getClothingCatalogUseCase = GetClothingCatalogUseCase(clothingRepository)
        val getClothingDetailsUseCase = GetClothingDetailsUseCase(clothingRepository)
        val getUserWardrobeUseCase = GetUserWardrobeUseCase(clothingRepository)
        val saveClothingToWardrobeUseCase = SaveClothingToWardrobeUseCase(clothingRepository)
        val recognizeClothingByPhotoUseCase = RecognizeClothingByPhotoUseCase(classifierRepository)
        val getUserProfileUseCase = GetUserProfileUseCase(userRepository)
        val savePreferredCityUseCase = SavePreferredCityUseCase(userRepository)

        val textUserStatus = findViewById<TextView>(R.id.textViewUserStatus)
        val editCity = findViewById<EditText>(R.id.editTextCity)
        val textOutput = findViewById<TextView>(R.id.textViewOutput)
        val buttonLoadWeather = findViewById<Button>(R.id.buttonLoadWeather)
        val buttonSaveWardrobe = findViewById<Button>(R.id.buttonSaveWardrobe)
        val buttonRecognizePhoto = findViewById<Button>(R.id.buttonRecognizePhoto)
        val buttonBackToAuth = findViewById<Button>(R.id.buttonBackToAuth)

        fun updateProfileHeader() {
            val profile = getUserProfileUseCase.execute()
            val roleText = if (profile.isAuthorized) {
                "Авторизован (${profile.email})"
            } else {
                "Режим Гостя (только просмотр)"
            }
            textUserStatus.text = "Профиль (SharedPreferences): $roleText | Любимый город: ${profile.preferredCity}"
        }

        fun loadWeatherAndCatalog(city: String) {
            val profile = getUserProfileUseCase.execute()
            if (profile.isAuthorized) {
                savePreferredCityUseCase.execute(city)
            }
            updateProfileHeader()

            val weather = getCurrentWeatherUseCase.execute(city)
            val recommended = getClothingCatalogUseCase.execute(weather.temperature)
            val detailsSample = getClothingDetailsUseCase.execute(1)

            val sb = StringBuilder()
            sb.appendLine("=== ПОГОДА (NetworkApi) ===")
            sb.appendLine("Город: ${weather.city}")
            sb.appendLine("Температура: ${weather.temperature}°C (${weather.condition}, ветер ${weather.windSpeed} м/с)")
            sb.appendLine("Совет: ${weather.recommendation}")
            sb.appendLine()
            sb.appendLine("=== ПОДБОР ИЗ КАТАЛОГА (${recommended.size} шт.) ===")
            recommended.forEach { item ->
                val badge = if (item.isFavorite) " [В гардеробе Room]" else ""
                sb.appendLine("• ${item.name} [${item.category}] (${item.minTemp}..${item.maxTemp}°C)$badge")
            }
            sb.appendLine()
            if (profile.isAuthorized) {
                val wardrobe = getUserWardrobeUseCase.execute()
                sb.appendLine("=== МОЙ ГАРДЕРОБ (БД Room: ${wardrobe.size} шт.) ===")
                wardrobe.forEach { item ->
                    sb.appendLine("★ ${item.name} — ${item.category}")
                }
            } else {
                sb.appendLine("=== МОЙ ГАРДЕРОБ (БД Room) ===")
                sb.appendLine("Недоступно в режиме Гостя. Войдите через Firebase Auth для работы с личным гардеробом.")
            }
            detailsSample?.let {
                sb.appendLine()
                sb.appendLine("=== ДЕТАЛИ ВЕЩИ #1 ===")
                sb.appendLine("${it.name}: ${it.description}")
            }
            textOutput.text = sb.toString()
        }

        val initialProfile = getUserProfileUseCase.execute()
        editCity.setText(initialProfile.preferredCity)
        loadWeatherAndCatalog(initialProfile.preferredCity)

        buttonLoadWeather.setOnClickListener {
            val city = editCity.text.toString().ifBlank { "Москва" }
            loadWeatherAndCatalog(city)
        }

        buttonSaveWardrobe.setOnClickListener {
            val profile = getUserProfileUseCase.execute()
            if (!profile.isAuthorized) {
                textOutput.text = "Ограничение доступа: Гость не может сохранять вещи в БД Room. Пожалуйста, войдите через экран авторизации."
                return@setOnClickListener
            }
            val newItem = ClothingItem(
                id = 1,
                name = "Мембранная ветровка Urban",
                category = "Верхняя одежда (Плащ / Пальто / Ветровка)",
                minTemp = 8,
                maxTemp = 18,
                imageUrl = "https://images.unsplash.com/photo-1551028719-00167b16eac5?w=600",
                description = "Лёгкая непродуваемая куртка с водоотталкивающей пропиткой.",
                isFavorite = true
            )
            val saved = saveClothingToWardrobeUseCase.execute(newItem)
            val wardrobe = getUserWardrobeUseCase.execute()
            textOutput.text = buildString {
                appendLine("Сохранение в БД Room выполнено: $saved")
                appendLine("Текущий личный гардероб из Room (${wardrobe.size} шт.):")
                wardrobe.forEach { item ->
                    appendLine("★ [#${item.id}] ${item.name} (${item.minTemp}..${item.maxTemp}°C)")
                }
            }
        }

        buttonRecognizePhoto.setOnClickListener {
            val profile = getUserProfileUseCase.execute()
            if (!profile.isAuthorized) {
                textOutput.text = "Ограничение доступа: ИИ-сканер доступен только авторизованным пользователям."
                return@setOnClickListener
            }
            val result = recognizeClothingByPhotoUseCase.execute("sample_coat_photo.jpg")
            textOutput.text = buildString {
                appendLine("=== РЕЗУЛЬТАТ AI-СКАНЕРА (TFLite) ===")
                appendLine("Распознанный класс: ${result.recognizedLabel}")
                appendLine("Уверенность модели: ${(result.confidence * 100).toInt()}%")
                appendLine("Категория: ${result.suggestedCategory}")
                appendLine("Рекомендуемая температура: ${result.suggestedMinTemp}..${result.suggestedMaxTemp}°C")
            }
        }

        buttonBackToAuth.setOnClickListener {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
    }
}
