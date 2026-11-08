package ru.mirea.svinarenkomd.domain.usecases

import ru.mirea.svinarenkomd.domain.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    fun execute(
        email: String,
        password: String,
        onResult: ((Boolean, String) -> Unit)? = null
    ): Boolean {
        if (email.isBlank() || password.length < 6) {
            onResult?.invoke(false, "Введите корректный email и пароль (минимум 6 символов)")
            return false
        }
        return authRepository.login(email.trim(), password, onResult)
    }
}
