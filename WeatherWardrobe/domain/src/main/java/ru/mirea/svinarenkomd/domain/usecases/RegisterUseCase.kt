package ru.mirea.svinarenkomd.domain.usecases

import ru.mirea.svinarenkomd.domain.repository.AuthRepository

class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    fun execute(
        email: String,
        password: String,
        onResult: ((Boolean, String) -> Unit)? = null
    ): Boolean {
        if (email.isBlank() || password.length < 6) {
            onResult?.invoke(false, "Для регистрации в Firebase пароль должен содержать минимум 6 символов")
            return false
        }
        return authRepository.register(email.trim(), password, onResult)
    }
}
