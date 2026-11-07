package ru.mirea.svinarenkomd.weatherwardrobe.domain.usecases

import ru.mirea.svinarenkomd.weatherwardrobe.domain.repository.AuthRepository

class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    fun execute(email: String, password: String): Boolean {
        if (email.isBlank() || password.length < 4) {
            return false
        }
        return authRepository.register(email.trim(), password)
    }
}
