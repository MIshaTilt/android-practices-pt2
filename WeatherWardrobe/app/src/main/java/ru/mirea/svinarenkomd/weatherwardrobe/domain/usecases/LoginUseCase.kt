package ru.mirea.svinarenkomd.weatherwardrobe.domain.usecases

import ru.mirea.svinarenkomd.weatherwardrobe.domain.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    fun execute(email: String, password: String): Boolean {
        if (email.isBlank() || password.isBlank()) {
            return false
        }
        return authRepository.login(email.trim(), password)
    }
}
