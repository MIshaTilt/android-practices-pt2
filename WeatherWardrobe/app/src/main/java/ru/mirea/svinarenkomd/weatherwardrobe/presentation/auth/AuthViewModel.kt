package ru.mirea.svinarenkomd.weatherwardrobe.presentation.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mirea.svinarenkomd.domain.models.UserProfile
import ru.mirea.svinarenkomd.domain.repository.AuthRepository
import ru.mirea.svinarenkomd.domain.repository.UserRepository
import ru.mirea.svinarenkomd.domain.usecases.GetUserProfileUseCase
import ru.mirea.svinarenkomd.domain.usecases.LoginUseCase
import ru.mirea.svinarenkomd.domain.usecases.LogoutUseCase
import ru.mirea.svinarenkomd.domain.usecases.RegisterUseCase

class AuthViewModel(
    authRepository: AuthRepository,
    userRepository: UserRepository
) : ViewModel() {

    private val loginUseCase = LoginUseCase(authRepository)
    private val registerUseCase = RegisterUseCase(authRepository)
    private val logoutUseCase = LogoutUseCase(authRepository)
    private val getUserProfileUseCase = GetUserProfileUseCase(userRepository)

    private val _userProfileState = MutableLiveData<UserProfile>()
    val userProfileState: LiveData<UserProfile> = _userProfileState

    private val _authMessageState = MutableLiveData<String>()
    val authMessageState: LiveData<String> = _authMessageState

    private val _navigateToMainEvent = MutableLiveData<Boolean>(false)
    val navigateToMainEvent: LiveData<Boolean> = _navigateToMainEvent

    init {
        Log.d(TAG, "AuthViewModel created")
        refreshProfile()
    }

    fun refreshProfile() {
        _userProfileState.value = getUserProfileUseCase.execute()
    }

    fun login(email: String, password: String) {
        _authMessageState.value = "Выполняется вход через Firebase Auth..."
        loginUseCase.execute(email, password) { success, message ->
            _authMessageState.value = message
            refreshProfile()
            if (success) {
                _navigateToMainEvent.value = true
            }
        }
    }

    fun register(email: String, password: String) {
        _authMessageState.value = "Создание аккаунта в Firebase Auth..."
        registerUseCase.execute(email, password) { success, message ->
            _authMessageState.value = message
            refreshProfile()
            if (success) {
                _navigateToMainEvent.value = true
            }
        }
    }

    fun continueAsGuest() {
        logoutUseCase.execute()
        refreshProfile()
        _authMessageState.value = "Вход в режиме Гостя"
        _navigateToMainEvent.value = true
    }

    fun onNavigatedToMain() {
        _navigateToMainEvent.value = false
    }

    override fun onCleared() {
        Log.d(TAG, "AuthViewModel cleared")
        super.onCleared()
    }

    companion object {
        private const val TAG = "AuthViewModel"
    }
}
