package ru.mirea.svinarenkomd.weatherwardrobe.presentation.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.mirea.svinarenkomd.data.network.NetworkApi
import ru.mirea.svinarenkomd.data.repository.AuthRepositoryImpl
import ru.mirea.svinarenkomd.data.repository.ClassifierRepositoryImpl
import ru.mirea.svinarenkomd.data.repository.ClothingRepositoryImpl
import ru.mirea.svinarenkomd.data.repository.UserRepositoryImpl
import ru.mirea.svinarenkomd.data.repository.WeatherRepositoryImpl
import ru.mirea.svinarenkomd.data.storage.room.RoomClothingStorage
import ru.mirea.svinarenkomd.data.storage.sharedprefs.SharedPrefUserStorage
import ru.mirea.svinarenkomd.weatherwardrobe.presentation.auth.AuthViewModel

class ViewModelFactory(
    context: Context
) : ViewModelProvider.Factory {

    private val appContext: Context = context.applicationContext

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val userStorage = SharedPrefUserStorage(appContext)
        val clothingStorage = RoomClothingStorage(appContext)
        val networkApi = NetworkApi()

        val clothingRepository = ClothingRepositoryImpl(clothingStorage, networkApi)
        val weatherRepository = WeatherRepositoryImpl(networkApi)
        val authRepository = AuthRepositoryImpl(userStorage)
        val userRepository = UserRepositoryImpl(userStorage)
        val classifierRepository = ClassifierRepositoryImpl()

        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(
                    clothingRepository = clothingRepository,
                    weatherRepository = weatherRepository,
                    userRepository = userRepository,
                    classifierRepository = classifierRepository
                ) as T
            }
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(
                    authRepository = authRepository,
                    userRepository = userRepository
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
