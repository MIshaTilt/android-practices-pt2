package ru.mirea.svinarenkomd.weatherwardrobe.presentation.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mirea.svinarenkomd.domain.models.ClothingItem
import ru.mirea.svinarenkomd.domain.models.RecognitionResult
import ru.mirea.svinarenkomd.domain.models.UserProfile
import ru.mirea.svinarenkomd.domain.models.WeatherInfo
import ru.mirea.svinarenkomd.domain.repository.ClassifierRepository
import ru.mirea.svinarenkomd.domain.repository.ClothingRepository
import ru.mirea.svinarenkomd.domain.repository.UserRepository
import ru.mirea.svinarenkomd.domain.repository.WeatherRepository
import ru.mirea.svinarenkomd.domain.usecases.GetClothingCatalogUseCase
import ru.mirea.svinarenkomd.domain.usecases.GetClothingDetailsUseCase
import ru.mirea.svinarenkomd.domain.usecases.GetCurrentWeatherUseCase
import ru.mirea.svinarenkomd.domain.usecases.GetUserProfileUseCase
import ru.mirea.svinarenkomd.domain.usecases.GetUserWardrobeUseCase
import ru.mirea.svinarenkomd.domain.usecases.RecognizeClothingByPhotoUseCase
import ru.mirea.svinarenkomd.domain.usecases.RemoveClothingFromWardrobeUseCase
import ru.mirea.svinarenkomd.domain.usecases.SaveClothingToWardrobeUseCase
import ru.mirea.svinarenkomd.domain.usecases.SavePreferredCityUseCase

class MainViewModel(
    clothingRepository: ClothingRepository,
    weatherRepository: WeatherRepository,
    userRepository: UserRepository,
    classifierRepository: ClassifierRepository
) : ViewModel() {

    private val getCurrentWeatherUseCase = GetCurrentWeatherUseCase(weatherRepository)
    private val getClothingCatalogUseCase = GetClothingCatalogUseCase(clothingRepository)
    private val getClothingDetailsUseCase = GetClothingDetailsUseCase(clothingRepository)
    private val getUserWardrobeUseCase = GetUserWardrobeUseCase(clothingRepository)
    private val saveClothingToWardrobeUseCase = SaveClothingToWardrobeUseCase(clothingRepository)
    private val removeClothingFromWardrobeUseCase = RemoveClothingFromWardrobeUseCase(clothingRepository)
    private val recognizeClothingByPhotoUseCase = RecognizeClothingByPhotoUseCase(classifierRepository)
    private val getUserProfileUseCase = GetUserProfileUseCase(userRepository)
    private val savePreferredCityUseCase = SavePreferredCityUseCase(userRepository)

    // 1. LiveData профиля пользователя (из SharedPreferences + FirebaseAuth)
    private val _userProfileState = MutableLiveData<UserProfile>()
    val userProfileState: LiveData<UserProfile> = _userProfileState

    // 2. LiveData текущей погоды (из сетевого источника NetworkApi)
    private val _weatherState = MutableLiveData<WeatherInfo>()
    val weatherState: LiveData<WeatherInfo> = _weatherState

    // 3. LiveData сетевого каталога одежды (из NetworkApi)
    private val _networkCatalogLiveData = MutableLiveData<List<ClothingItem>>()
    val networkCatalogState: LiveData<List<ClothingItem>> = _networkCatalogLiveData

    // 4. LiveData личного гардероба из локальной БД Room
    private val _roomWardrobeLiveData = MutableLiveData<List<ClothingItem>>()
    val roomWardrobeState: LiveData<List<ClothingItem>> = _roomWardrobeLiveData

    // 5. LiveData результата распознавания TFLite
    private val _recognitionState = MutableLiveData<RecognitionResult?>()
    val recognitionState: LiveData<RecognitionResult?> = _recognitionState

    // 6. LiveData информационных сообщений / статуса действий
    private val _actionStatusState = MutableLiveData<String>()
    val actionStatusState: LiveData<String> = _actionStatusState

    // 7. MediatorLiveData — объединяет данные из замоканной сетевой части (NetworkApi) и локальной БД (Room)
    val combinedWardrobeState = MediatorLiveData<List<ClothingItem>>()

    // 8. MediatorLiveData — формирует сводное текстовое состояние экрана из нескольких LiveData
    val dashboardTextState = MediatorLiveData<String>()

    init {
        Log.d(TAG, "MainViewModel created")

        val mergeSources = {
            val networkList = _networkCatalogLiveData.value.orEmpty()
            val roomList = _roomWardrobeLiveData.value.orEmpty()
            val roomIds = roomList.map { it.id }.toSet()

            // Отмечаем вещи из сетевого каталога, которые уже сохранены в БД Room,
            // и добавляем вещи из Room, не попавшие в сетевую выборку
            val merged = mutableListOf<ClothingItem>()
            networkList.forEach { netItem ->
                merged.add(netItem.copy(isFavorite = netItem.id in roomIds))
            }
            roomList.forEach { roomItem ->
                if (merged.none { it.id == roomItem.id }) {
                    merged.add(roomItem.copy(isFavorite = true))
                }
            }
            combinedWardrobeState.value = merged
        }

        combinedWardrobeState.addSource(_networkCatalogLiveData) { mergeSources() }
        combinedWardrobeState.addSource(_roomWardrobeLiveData) { mergeSources() }

        val rebuildDashboard = {
            val profile = _userProfileState.value
            val weather = _weatherState.value
            val networkItems = _networkCatalogLiveData.value.orEmpty()
            val roomItems = _roomWardrobeLiveData.value.orEmpty()
            val combinedItems = combinedWardrobeState.value.orEmpty()
            val detailsSample = getClothingDetailsUseCase.execute(1)

            if (weather != null && profile != null) {
                val sb = StringBuilder()
                sb.appendLine("=== ПОГОДА (LiveData <- NetworkApi) ===")
                sb.appendLine("Город: ${weather.city}")
                sb.appendLine("Температура: ${weather.temperature}°C (${weather.condition}, ветер ${weather.windSpeed} м/с)")
                sb.appendLine("Совет: ${weather.recommendation}")
                sb.appendLine()
                sb.appendLine("=== МЕДИАТОР (MediatorLiveData: Сеть + БД Room = ${combinedItems.size} шт.) ===")
                sb.appendLine("Из сети по погоде: ${networkItems.size} шт. | В БД Room: ${roomItems.size} шт.")
                combinedItems.forEach { item ->
                    val sourceTag = if (item.isFavorite) "[В БД Room ★]" else "[Из сети NetworkApi]"
                    sb.appendLine("• ${item.name} (${item.minTemp}..${item.maxTemp}°C) $sourceTag")
                }
                sb.appendLine()
                if (profile.isAuthorized) {
                    sb.appendLine("=== ЛИЧНЫЙ ГАРДЕРОБ (Room DB: ${roomItems.size} шт.) ===")
                    roomItems.forEach { item ->
                        sb.appendLine("★ [#${item.id}] ${item.name} — ${item.category}")
                    }
                } else {
                    sb.appendLine("=== ЛИЧНЫЙ ГАРДЕРОБ (Room DB) ===")
                    sb.appendLine("Режим Гостя: сохранение в БД Room и ИИ-сканер недоступны.")
                }
                detailsSample?.let {
                    sb.appendLine()
                    sb.appendLine("=== СТРАНИЦА СУЩНОСТИ #1 ===")
                    sb.appendLine("${it.name}: ${it.description}")
                }
                dashboardTextState.value = sb.toString()
            }
        }

        dashboardTextState.addSource(_weatherState) { rebuildDashboard() }
        dashboardTextState.addSource(combinedWardrobeState) { rebuildDashboard() }
        dashboardTextState.addSource(_userProfileState) { rebuildDashboard() }

        // Первичная загрузка данных при создании ViewModel (при повороте экрана повторно не вызывается)
        val initialProfile = getUserProfileUseCase.execute()
        _userProfileState.value = initialProfile
        loadWeatherAndCatalog(initialProfile.preferredCity)
    }

    fun loadWeatherAndCatalog(cityInput: String) {
        val city = cityInput.trim().ifEmpty { "Москва" }
        val profile = getUserProfileUseCase.execute()
        if (profile.isAuthorized) {
            savePreferredCityUseCase.execute(city)
        }
        _userProfileState.value = getUserProfileUseCase.execute()

        val weather = getCurrentWeatherUseCase.execute(city)
        _weatherState.value = weather

        // Обновляем оба источника для MediatorLiveData: сетевой каталог и локальную БД Room
        _networkCatalogLiveData.value = getClothingCatalogUseCase.execute(weather.temperature)
        _roomWardrobeLiveData.value = if (profile.isAuthorized) {
            getUserWardrobeUseCase.execute()
        } else {
            emptyList()
        }
    }

    fun saveWindbreakerToWardrobe() {
        val profile = getUserProfileUseCase.execute()
        if (!profile.isAuthorized) {
            _actionStatusState.value =
                "Ограничение доступа: Гость не может сохранять вещи в БД Room. Войдите через экран авторизации."
            return
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
        // Обновляем источник Room — MediatorLiveData автоматически пересчитает объединённый список!
        _roomWardrobeLiveData.value = getUserWardrobeUseCase.execute()
        _actionStatusState.value =
            "Вещь «${newItem.name}» сохранена в БД Room ($saved). MediatorLiveData обновил список!"
    }

    fun removeFromWardrobe(itemId: Int) {
        val profile = getUserProfileUseCase.execute()
        if (!profile.isAuthorized) return
        removeClothingFromWardrobeUseCase.execute(itemId)
        _roomWardrobeLiveData.value = getUserWardrobeUseCase.execute()
    }

    fun recognizeSamplePhoto() {
        val profile = getUserProfileUseCase.execute()
        if (!profile.isAuthorized) {
            _actionStatusState.value =
                "Ограничение доступа: ИИ-сканер TFLite доступен только авторизованным пользователям."
            return
        }
        val result = recognizeClothingByPhotoUseCase.execute("sample_coat_photo.jpg")
        _recognitionState.value = result
        _actionStatusState.value =
            "AI TFLite распознал: ${result.recognizedLabel} (${(result.confidence * 100).toInt()}%), " +
                "рекомендуемая температура ${result.suggestedMinTemp}..${result.suggestedMaxTemp}°C"
    }

    override fun onCleared() {
        Log.d(TAG, "MainViewModel cleared")
        super.onCleared()
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}
